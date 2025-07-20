using DTOs.DTO;
using Microsoft.AspNetCore.Mvc.Rendering;

namespace BookStoreWebMVC.Models.ViewModel
{
    public class BookCreateEditViewModel
    {
        public BookDto Book { get; set; } = new BookDto();

        public List<SelectListItem> Categories { get; set; } = new();
        public List<SelectListItem> Authors { get; set; } = new();
    }
}
