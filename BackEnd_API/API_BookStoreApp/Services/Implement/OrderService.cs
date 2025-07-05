using BusinessObject.Models;
using DataAccessObject;
using DTOs.DTO;
using Services.Interface;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Services.Implement
{
    public class OrderService : IOrderService
    {
        private readonly OrderDAO _orderDAO;
        private readonly BookDAO _bookDAO;

        public OrderService(OrderDAO orderDAO, BookDAO bookDAO)
        {
            _orderDAO = orderDAO;
            _bookDAO = bookDAO;
        }


        public async Task<OrderDTO> CreateOrderAsync(OrderCreateDTO request)
        {
            // Tạo Order mới (chưa có TotalAmount)
            var order = new Order
            {
                UserId = request.UserId,
                OrderDate = DateTime.UtcNow,
                TotalAmount = 0, // sẽ tính sau
                ShippingAddress = request.ShippingAddress,
                Status = string.IsNullOrEmpty(request.Status) ? "Chưa thanh toán" : request.Status
            };

            // Tạo danh sách OrderItems từ DTO
            var orderItems = new List<OrderItem>();

            foreach (var item in request.Items)
            {
                var book = await _bookDAO.GetBookByIdAsync(item.BookId); // lấy giá

                if (book == null)
                    throw new Exception($"Book with ID {item.BookId} not found");

                // ✅ Kiểm tra tồn kho tại đây
                if (book.Stock < item.Quantity)
                    throw new Exception($"Not enough stock for book '{book.Title}'. Only {book.Stock} left.");


                orderItems.Add(new OrderItem
                {
                    BookId = item.BookId,
                    Quantity = item.Quantity,
                    UnitPrice = book.Price
                });

                order.TotalAmount += book.Price * item.Quantity;
            }

            // Gọi DAO để lưu vào DB
            var createdOrder = await _orderDAO.CreateOrderAsync(order, orderItems);

            // Trả về OrderDTO
            return new OrderDTO
            {
                OrderId = createdOrder.OrderId,
                OrderDate = createdOrder.OrderDate,
                TotalAmount = createdOrder.TotalAmount,
                Items = createdOrder.OrderItems.Select(oi => new OrderItemDTO
                {
                    OrderItemId = oi.OrderItemId,
                    BookId = oi.BookId,
                    Quantity = oi.Quantity,
                    UnitPrice = oi.UnitPrice,
                    BookTitle = oi.Book?.Title // nếu bạn include Book trong DAO
                }).ToList()
            };
        }
        public async Task<List<OrderDTO>> GetOrdersByUserIdAsync(int userId)
        {
            var orders = await _orderDAO.GetOrdersByUserIdAsync(userId);
            return orders.Select(MapToOrderDTO).ToList();
        }

        public async Task<List<OrderDTO>> GetAllOrdersAsync()
        {
            var orders = await _orderDAO.GetAllOrdersAsync();
            return orders.Select(MapToOrderDTO).ToList();
        }
        // Helper method to map Order to DTO
        private OrderDTO MapToOrderDTO(Order order)
        {
            return new OrderDTO
            {
                OrderId = order.OrderId,
                OrderDate = order.OrderDate,
                TotalAmount = order.TotalAmount,
                ShippingAddress = order.ShippingAddress,
                Status = order.Status,
                Items = order.OrderItems.Select(oi => new OrderItemDTO
                {
                    OrderItemId = oi.OrderItemId,
                    BookId = oi.BookId,
                    Quantity = oi.Quantity,
                    UnitPrice = oi.UnitPrice,
                    BookTitle = oi.Book?.Title,
                    ImgUrl = oi.Book?.ImageUrl,
                }).ToList()
            };
        }

    }
}
