using BookStoreWebMVC.Models;
using DTOs.DTO;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;

namespace BookStoreWebMVC.Controllers
{
    public class AuthorManageController : Controller
    {
        private readonly IHttpClientFactory _httpClientFactory;
        private readonly ApiSettings _apiSettings;

        public AuthorManageController(IHttpClientFactory httpClientFactory, IOptions<ApiSettings> options)
        {
            _httpClientFactory = httpClientFactory;
            _apiSettings = options.Value;
        }

        public async Task<IActionResult> Index()
        {
            var client = _httpClientFactory.CreateClient();
            var authors = await client.GetFromJsonAsync<List<AuthorDto>>($"{_apiSettings.BaseUrl}/api/Author");
            return View(authors);
        }

        public async Task<IActionResult> Create()
        {
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Create(AuthorDto dto)
        {
            var client = _httpClientFactory.CreateClient();
            var response = await client.PostAsJsonAsync($"{_apiSettings.BaseUrl}/api/Author", dto);
            if (response.IsSuccessStatusCode)
                return RedirectToAction("Index");

            ViewBag.Error = "Thêm tác giả thất bại.";
            return View(dto);
        }

        public async Task<IActionResult> Edit(int id)
        {
            var client = _httpClientFactory.CreateClient();
            var author = await client.GetFromJsonAsync<AuthorDto>($"{_apiSettings.BaseUrl}/api/Author/{id}");
            return View(author);
        }

        [HttpPost]
        public async Task<IActionResult> Edit(AuthorDto dto)
        {
            var client = _httpClientFactory.CreateClient();
            var response = await client.PutAsJsonAsync($"{_apiSettings.BaseUrl}/api/Author/{dto.AuthorId}", dto);
            if (response.IsSuccessStatusCode)
                return RedirectToAction("Index");

            ViewBag.Error = "Cập nhật thất bại.";
            return View(dto);
        }

        public async Task<IActionResult> Delete(int id)
        {
            var client = _httpClientFactory.CreateClient();
            await client.DeleteAsync($"{_apiSettings.BaseUrl}/api/Author/{id}");
            return RedirectToAction("Index");
        }
}
}
