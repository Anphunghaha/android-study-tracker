using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DTOs;
using DTOs.DTO;
namespace Services.Interface
{
    public interface IBookService
    {
        Task<List<BookDto>> GetAllBooksAsync();
        Task<BookDto> GetBookByIdAsync(int id);
        Task<List<BookDto>> SearchBooksAsync(string keyword);
        Task<List<BookDto>> GetBooksByCategoryAsync(int categoryId);
        Task AddBookAsync(BookDto book);
        Task UpdateBookAsync(BookDto book, int idFromRoute);
        Task DeleteBookAsync(int bookId);
    }
}
