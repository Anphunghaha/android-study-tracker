using BookStoreWebMVC.Models.ViewModel;
using BookStoreWebMVC.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using System.Text.Json;
using System.Text;

namespace BookStoreWebMVC.Controllers
{
    public class AccountController : Controller
    {
        private readonly HttpClient _httpClient;
        private readonly string _apiBaseUrl;

        public AccountController(IHttpClientFactory httpClientFactory, IOptions<ApiSettings> options)
        {
            _httpClient = httpClientFactory.CreateClient();
            _apiBaseUrl = options.Value.BaseUrl;
        }

        [HttpGet]
        public IActionResult Login()
        {
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Login(LoginViewModel model)
        {
            if (!ModelState.IsValid) return View(model);

            var jsonContent = new StringContent(JsonSerializer.Serialize(model), Encoding.UTF8, "application/json");

            var response = await _httpClient.PostAsync($"{_apiBaseUrl}/api/user/login", jsonContent);

            if (!response.IsSuccessStatusCode)
            {
                ViewBag.Error = "Sai email hoặc mật khẩu";
                return View(model);
            }

            var json = await response.Content.ReadAsStringAsync();
            var user = JsonSerializer.Deserialize<UserResponse>(json, new JsonSerializerOptions
            {
                PropertyNameCaseInsensitive = true
            });

            if (user == null || user.Role?.ToLower() != "admin")
            {
                ViewBag.Error = "Bạn không có quyền truy cập (không phải Admin)";
                return View(model);
            }

            // Lưu vào session
            HttpContext.Session.SetInt32("UserId", user.UserId);
            HttpContext.Session.SetString("FullName", user.FullName ?? user.Username ?? user.Email);

            return RedirectToAction("Dashboard", "Admin");
        }
    }
}
