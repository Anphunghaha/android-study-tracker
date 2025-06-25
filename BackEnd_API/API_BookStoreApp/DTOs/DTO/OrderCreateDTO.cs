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
        public List<OrderItemCreateDTO> Items { get; set; }

    }
}
