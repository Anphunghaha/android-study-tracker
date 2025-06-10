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
                OrderDate = DateTime.UtcNow,
                TotalAmount = 0, // sẽ tính sau
            };

            // Tạo danh sách OrderItems từ DTO
            var orderItems = new List<OrderItem>();

            foreach (var item in request.Items)
            {
                var book = await _bookDAO.GetBookByIdAsync(item.BookId); // lấy giá

                if (book == null)
                    throw new Exception($"Book with ID {item.BookId} not found");

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

    }
}
