using BookStoreWebMVC.Models;
using BookStoreWebMVC.Models.ViewModel;
using DTOs.DTO;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;

namespace BookStoreWebMVC.Controllers
{
    public class AdminController : Controller
    {
        private readonly IHttpClientFactory _httpClientFactory;
        private readonly ApiSettings _apiSettings;

        public AdminController(IHttpClientFactory httpClientFactory, IOptions<ApiSettings> options)
        {
            _httpClientFactory = httpClientFactory;
            _apiSettings = options.Value;
        }

        public async Task<IActionResult> Dashboard()
        {
            var client = _httpClientFactory.CreateClient();
            var baseUrl = _apiSettings.BaseUrl;

            // Fetch data from APIs
            var bookResponse = await client.GetAsync($"{baseUrl}/api/Books");
            var orderResponse = await client.GetAsync($"{baseUrl}/api/Order/GetAllOrders");
            var categoryResponse = await client.GetAsync($"{baseUrl}/api/Category");
            var authorResponse = await client.GetAsync($"{baseUrl}/api/Author");

            // Deserialize responses
            var books = JsonConvert.DeserializeObject<List<BookDto>>(await bookResponse.Content.ReadAsStringAsync()) ?? new List<BookDto>();
            var orders = JsonConvert.DeserializeObject<List<OrderDTO>>(await orderResponse.Content.ReadAsStringAsync()) ?? new List<OrderDTO>();
            var categories = JsonConvert.DeserializeObject<List<CategoryDTO>>(await categoryResponse.Content.ReadAsStringAsync()) ?? new List<CategoryDTO>();
            var authors = JsonConvert.DeserializeObject<List<AuthorDto>>(await authorResponse.Content.ReadAsStringAsync()) ?? new List<AuthorDto>();

            // Calculate category book counts
            var categoryBookCounts = categories.ToDictionary(
                c => c.Name,
                c => books.Count(b => b.CategoryId == c.CategoryId)
            );
            // Calculate category sales counts
            var categorySalesCounts = categories.ToDictionary(
                c => c.Name,
                c => orders.Sum(o => o.Items?.Where(item => books.Any(b => b.BookID == item.BookId && b.CategoryId == c.CategoryId)).Sum(item => item.Quantity) ?? 0)
            );

            // Calculate total books sold (sum of quantities from OrderItemDTO)
            int totalBooksSold = orders.Sum(o => o.Items?.Sum(item => item.Quantity) ?? 0);
            int totalStock = books.Sum(b => b.Stock ?? 0); // Assumes BookDto has a Stock property

            // Populate ViewModel
            var viewModel = new DashboardViewModel
            {
                TotalBooks = books.Count,
                TotalOrders = orders.Count,
                TotalRevenue = (double)orders.Sum(o => o.TotalAmount ?? 0),
                TotalCategories = categories.Count,
                TotalAuthors = authors.Count,
                TotalBooksSold = totalBooksSold,
                TotalStock = totalStock,
                CategoryBookCounts = categoryBookCounts,
                CategorySalesCounts = categorySalesCounts
            };

            return View(viewModel);
        }
    }
}