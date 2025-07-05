using DTOs.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Services.Interface
{
    public interface IOrderService
    {
        Task<OrderDTO> CreateOrderAsync(OrderCreateDTO request);
        Task<List<OrderDTO>> GetOrdersByUserIdAsync(int userId);
        Task<List<OrderDTO>> GetAllOrdersAsync();
    }
}
