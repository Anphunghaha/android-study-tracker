using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DTOs.DTO
{
    public class OrderCreateDTO
    {
        public int UserId { get; set; } // 👈 thêm dòng này
        public string ShippingAddress { get; set; }
        public string Status { get; set; }  // Có thể là "Chờ xử lý", "Đang giao", v.v.

        public List<OrderItemCreateDTO> Items { get; set; }

    }
}
