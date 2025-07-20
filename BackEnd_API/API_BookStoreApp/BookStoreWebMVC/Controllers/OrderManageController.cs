using BookStoreWebMVC.Models;
using DTOs.DTO;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using System.Net.Http;
using System.Text;
using System.Text.Json;

namespace BookStoreWebMVC.Controllers
{
    public class OrderManageController : Controller
    {
        private readonly IHttpClientFactory _httpClientFactory;
        private readonly ApiSettings _apiSettings;

        public OrderManageController(IHttpClientFactory httpClientFactory, IOptions<ApiSettings> options)
        {
            _httpClientFactory = httpClientFactory;
            _apiSettings = options.Value;
        }

        public async Task<IActionResult> Index()
        {
            var client = _httpClientFactory.CreateClient();
            var orders = await client.GetFromJsonAsync<List<OrderWithDetailsDTO>>($"{_apiSettings.BaseUrl}/api/Order/GetAllOrders");

            return View(orders);

        }
        [HttpPost]
        public async Task<IActionResult> UpdateStatus(int orderId, string newStatus)
        {
            // Nếu bạn muốn chặn ở phía client khi trạng thái đã là "Completed", có thể truyền thêm điều kiện ở đây
            if (newStatus == "Completed")
            {
                TempData["Error"] = "Không thể sửa trạng thái đơn hàng đã hoàn thành!";
                return RedirectToAction("Index");
            }

            var client = _httpClientFactory.CreateClient();
            var content = new StringContent(JsonSerializer.Serialize(newStatus), Encoding.UTF8, "application/json");

            var response = await client.PutAsync($"{_apiSettings.BaseUrl}/api/order/updatestatus/{orderId}", content);

            if (response.IsSuccessStatusCode)
            {
                TempData["Success"] = "Cập nhật trạng thái thành công!";
            }
            else
            {
                TempData["Error"] = "Cập nhật thất bại!";
            }

            return RedirectToAction("Index");
        }


    }
}
