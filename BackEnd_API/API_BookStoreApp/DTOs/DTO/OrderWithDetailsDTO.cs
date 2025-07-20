using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DTOs.DTO
{
    public class OrderWithDetailsDTO
    {
        public int OrderId { get; set; }
        public DateTime OrderDate { get; set; }
        public string Status { get; set; } = null!;
        public string UserName { get; set; } = null!;
        public List<OrderDetailDTO> Items { get; set; } = new();
    }
    public class OrderDetailDTO
    {
        public string BookTitle { get; set; } // Thêm để hiển thị tên sách
        public int Quantity { get; set; }
        public decimal UnitPrice { get; set; }
    }

}
