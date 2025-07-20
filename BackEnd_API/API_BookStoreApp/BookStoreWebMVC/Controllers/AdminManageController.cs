using BookStoreWebMVC.Models;
using BookStoreWebMVC.Models.ViewModel;
using DTOs.DTO;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.Extensions.Options;

namespace BookStoreWebMVC.Controllers
{
    public class AdminManageController : Controller
    {
        private readonly HttpClient _httpClient;
        private readonly string _apiBaseUrl;

        public AdminManageController(IHttpClientFactory httpClientFactory, IOptions<ApiSettings> apiSettings)
        {
            _httpClient = httpClientFactory.CreateClient();
            _apiBaseUrl = apiSettings.Value.BaseUrl;
        }
        public async Task<IActionResult> Index()
        {
            var response = await _httpClient.GetFromJsonAsync<List<BookDto>>($"{_apiBaseUrl}/api/Books");
            return View(response); return View(response);
        }

        [HttpGet]
        public async Task<IActionResult> Create()
        {
            var viewModel = new BookCreateEditViewModel();

            // Gọi API lấy danh sách categories
            var categories = await _httpClient.GetFromJsonAsync<List<CategoryDTO>>($"{_apiBaseUrl}/api/Category");
            viewModel.Categories = categories.Select(c => new SelectListItem
            {
                Value = c.CategoryId.ToString(),
                Text = c.Name,
            }).ToList();

            // Gọi API lấy danh sách authors
            var authors = await _httpClient.GetFromJsonAsync<List<AuthorDto>>($"{_apiBaseUrl}/api/Author");
            viewModel.Authors = authors.Select(a => new SelectListItem
            {
                Value = a.AuthorId.ToString(),
                Text = a.Name,
            }).ToList();

            return View(viewModel);
        }
        [HttpPost]
        public async Task<IActionResult> Create(BookCreateEditViewModel model)
        {
            if (!ModelState.IsValid)
            {
                await LoadCategoriesAndAuthors(model);
                return View(model);
            }

            var response = await _httpClient.PostAsJsonAsync($"{_apiBaseUrl}/api/Books", model.Book);

            if (response.IsSuccessStatusCode)
                return RedirectToAction("Index");

            var errorContent = await response.Content.ReadAsStringAsync();
            ModelState.AddModelError("", $"Thêm sách thất bại: {errorContent}");

            await LoadCategoriesAndAuthors(model); // đảm bảo dropdown còn hoạt động khi có lỗi
            return View(model);
        }

        private async Task LoadCategoriesAndAuthors(BookCreateEditViewModel model)
        {
            var categories = await _httpClient.GetFromJsonAsync<List<CategoryDTO>>($"{_apiBaseUrl}/api/Category");
            model.Categories = categories.Select(c => new SelectListItem
            {
                Value = c.CategoryId.ToString(),
                Text = c.Name,
            }).ToList();

            var authors = await _httpClient.GetFromJsonAsync<List<AuthorDto>>($"{_apiBaseUrl}/api/Author");
            model.Authors = authors.Select(a => new SelectListItem
            {
                Value = a.AuthorId.ToString(),
                Text = a.Name,
            }).ToList();
        }


        [HttpGet]
        public async Task<IActionResult> Edit(int id)
        {
            var book = await _httpClient.GetFromJsonAsync<BookDto>($"{_apiBaseUrl}/api/Books/{id}");
            if (book == null) return NotFound();

            var authors = await _httpClient.GetFromJsonAsync<List<AuthorDto>>($"{_apiBaseUrl}/api/Author");
            var categories = await _httpClient.GetFromJsonAsync<List<CategoryDTO>>($"{_apiBaseUrl}/api/Category");

            var vm = new BookCreateEditViewModel
            {
                Book = book,
                Authors = authors.Select(a => new SelectListItem { Value = a.AuthorId.ToString(), Text = a.Name }).ToList(),
                Categories = categories.Select(c => new SelectListItem { Value = c.CategoryId.ToString(), Text = c.Name }).ToList()
            };

            return View(vm);
        }

        [HttpPost]
        [HttpPost]
        public async Task<IActionResult> Edit(BookCreateEditViewModel model)
        {
            var response = await _httpClient.PutAsJsonAsync($"{_apiBaseUrl}/api/Books/{model.Book.BookID}", model.Book); // ✅ chỉ gửi BookDto

            if (response.IsSuccessStatusCode)
                return RedirectToAction("Index");

            var error = await response.Content.ReadAsStringAsync(); // để hiển thị chi tiết nếu cần debug
            ModelState.AddModelError("", "Cập nhật thất bại. " + error);
            await LoadCategoriesAndAuthors(model);
            return View(model);
        }



        public async Task<IActionResult> Delete(int id)
        {
            var response = await _httpClient.DeleteAsync($"{_apiBaseUrl}/api/Books/{id}");

            if (response.IsSuccessStatusCode)
            {
                return RedirectToAction("Index");
            }

            TempData["Error"] = "Xóa thất bại";
            return RedirectToAction("Index");
        }


    }
}
