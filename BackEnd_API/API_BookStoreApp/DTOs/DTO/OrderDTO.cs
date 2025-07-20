using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DTOs.DTO
{
    public class OrderDTO
    {
        public int OrderId { get; set; }
        public DateTime? OrderDate { get; set; }
        public decimal? TotalAmount { get; set; }
        public string ShippingAddress { get; set; }
        public string Status { get; set; }
        public int? UserId { get; set; }  
        public string? UserName { get; set; }

        public List<OrderItemDTO> Items { get; set; }
    }

    public class OrderItemDTO
    {
        public int OrderItemId { get; set; }
        public int BookId { get; set; }
        public string BookTitle { get; set; } // Thêm để hiển thị tên sách
        public int Quantity { get; set; }
        public decimal UnitPrice { get; set; }
        public string ImgUrl { get; set; } // ✅ Thêm dòng này

    }
}
