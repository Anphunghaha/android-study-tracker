﻿using BusinessObject.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessObject
{
    public class OrderDAO
    {
        private readonly BookStoreAppContext _context;

        public OrderDAO(BookStoreAppContext context)
        {
            _context = context;
        }
        public async Task<Order> CreateOrderAsync(Order order, List<OrderItem> items)
        {
            _context.Orders.Add(order);
            await _context.SaveChangesAsync();

            foreach (var item in items)
            {
                item.OrderId = order.OrderId;
                _context.OrderItems.Add(item);

                // ✅ Trừ tồn kho cho mỗi sách
                var book = await _context.Books.FindAsync(item.BookId);
                if (book == null)
                {
                    throw new Exception($"Book with ID {item.BookId} not found when updating stock.");
                }
                book.Stock -= item.Quantity;
                _context.Books.Update(book);
            }

            await _context.SaveChangesAsync();

            // Optional: load lại để include Book
            await _context.Entry(order)
                .Collection(o => o.OrderItems)
                .Query()
                .Include(oi => oi.Book)
                .LoadAsync();

            return order;
        }
        public async Task<List<Order>> GetOrdersByUserIdAsync(int userId)
        {
            return await _context.Orders
                .Where(o => o.UserId == userId)
                .Include(o => o.OrderItems)
                    .ThenInclude(oi => oi.Book)
                .OrderByDescending(o => o.OrderDate)
                .ToListAsync();
        }
        public async Task<List<Order>> GetAllOrdersAsync()
        {
            return await _context.Orders
                .Include(o => o.User) // Nếu bạn có navigation tới User
                .Include(o => o.OrderItems)
                    .ThenInclude(oi => oi.Book)
                .OrderByDescending(o => o.OrderDate)
                .ToListAsync();
        }
        public async Task<Order?> GetOrderByIdAsync(int orderId)
        {
            return await _context.Orders.FirstOrDefaultAsync(o => o.OrderId == orderId);
        }
        public async Task UpdateOrderAsync(Order order)
        {
            _context.Orders.Update(order);
            await _context.SaveChangesAsync();
        }


    }
}
