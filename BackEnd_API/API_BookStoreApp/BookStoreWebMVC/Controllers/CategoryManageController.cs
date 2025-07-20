using BookStoreWebMVC.Models;
using DTOs.DTO;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;
using System.Text;

namespace BookStoreWebMVC.Controllers
{
    public class CategoryManageController : Controller
    {
        private readonly IHttpClientFactory _httpClientFactory;
        private readonly ApiSettings _apiSettings;

        public CategoryManageController(IHttpClientFactory httpClientFactory, IOptions<ApiSettings> options)
        {
            _httpClientFactory = httpClientFactory;
            _apiSettings = options.Value;
        }
        public async Task<IActionResult> Index()
        {
            var client = _httpClientFactory.CreateClient();
            var response = await client.GetAsync($"{_apiSettings.BaseUrl}/api/Category");

            if (!response.IsSuccessStatusCode)
                return View(new List<CategoryDTO>());

            var categories = await response.Content.ReadFromJsonAsync<List<CategoryDTO>>();
            return View(categories);
        }
        [HttpGet]
        public IActionResult Create()
        {
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Create(CategoryDTO model)
        {
            if (!ModelState.IsValid)
                return View(model);

            var client = _httpClientFactory.CreateClient();
            var content = new StringContent(JsonConvert.SerializeObject(model), Encoding.UTF8, "application/json");
            var response = await client.PostAsync($"{_apiSettings.BaseUrl}/api/Category", content);

            if (response.IsSuccessStatusCode)
                return RedirectToAction("Index");

            ModelState.AddModelError("", "Thêm mới thất bại.");
            return View(model);
        }

        [HttpGet]
        public async Task<IActionResult> Edit(int id)
        {
            var client = _httpClientFactory.CreateClient();
            var response = await client.GetAsync($"{_apiSettings.BaseUrl}/api/Category/{id}");

            if (!response.IsSuccessStatusCode) return NotFound();

            var data = await response.Content.ReadFromJsonAsync<CategoryDTO>();
            return View(data);
        }

        [HttpPost]
        public async Task<IActionResult> Edit(CategoryDTO dto)
        {
            var client = _httpClientFactory.CreateClient();
            var response = await client.PutAsJsonAsync($"{_apiSettings.BaseUrl}/api/Category/{dto.CategoryId}", dto);

            if (response.IsSuccessStatusCode)
                return RedirectToAction("Index");

            ModelState.AddModelError("", "Cập nhật thất bại.");
            return View(dto);
        }
        [HttpGet]
        public async Task<IActionResult> Delete(int id)
        {
            var client = _httpClientFactory.CreateClient();
            var response = await client.DeleteAsync($"{_apiSettings.BaseUrl}/api/Category/{id}");

            return RedirectToAction("Index");
        }

    }
}
