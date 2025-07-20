namespace BookStoreWebMVC.Models.ViewModel
{
    public class DashboardViewModel
    {
        public int TotalBooks { get; set; }
        public int TotalOrders { get; set; }
        public double TotalRevenue { get; set; }
        public int TotalCategories { get; set; }
        public int TotalAuthors { get; set; }
        public Dictionary<string, int> CategoryBookCounts { get; set; } = new();
        public Dictionary<string, int> CategorySalesCounts { get; set; } = new(); // New: Books sold per category
        public int TotalBooksSold { get; set; } // Number of books sold (sum of quantities from OrderItemDTO)

        public int TotalStock { get; set; } // Sum of BookDto.Stock (total inventory)
    }
}
