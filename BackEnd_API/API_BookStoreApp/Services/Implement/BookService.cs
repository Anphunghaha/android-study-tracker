using BusinessObject.Models;
using DataAccessObject;
using DTOs.DTO;
using Microsoft.EntityFrameworkCore;
using Services.Interface;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Services.Implement
{
    public class BookService : IBookService
    {
        private readonly BookDAO _bookDAO;

        public BookService(BookDAO bookDAO)
        {
            _bookDAO = bookDAO;
        }
        public async Task AddBookAsync(BookDto dto)
        {
            var book = MapToEntity(dto);
            _bookDAO.AddBookAsync(book); // DAO bạn dùng synchronous, có thể chỉnh lại dùng async
        }

        public async Task DeleteBookAsync(int bookId)
        {
            var book = await _bookDAO.GetBookByIdAsync(bookId);
            if (book != null)
            {
                _bookDAO.DeleteBook(book);
            }
        }

        public async Task<List<BookDto>> GetAllBooksAsync()
        {
            var books = await _bookDAO.GetAllBooksAsync();
            return books.Select(MapToDTO).ToList();
        }


        public async Task<BookDto> GetBookByIdAsync(int id)
        {
            var book = await _bookDAO.GetBookByIdAsync(id);
            return book != null ? MapToDTO(book) : null;
        }

        public async Task<List<BookDto>> GetBooksByCategoryAsync(int categoryId)
        {
            var books = await _bookDAO.GetBooksByCategoryAsync(categoryId);
            return books.Select(MapToDTO).ToList();
        }

        public async Task<List<BookDto>> SearchBooksAsync(string keyword)
        {
            var books = await _bookDAO.SearchBooksAsync(keyword);
            return books.Select(MapToDTO).ToList();
        }

        public async Task UpdateBookAsync(BookDto dto, int idFromRoute)
        {
            var existingBook = await _bookDAO.GetBookByIdAsync(idFromRoute);
            if (existingBook == null)
            {
                throw new Exception("Book not found.");
            }

            // Cập nhật các trường khác, KHÔNG cập nhật ID
            existingBook.Title = dto.Title;
            existingBook.Description = dto.Description;
            existingBook.Price = dto.Price;
            existingBook.Stock = dto.Stock;
            existingBook.ImageUrl = dto.ImageUrl;
            existingBook.AuthorId = dto.AuthorId;
            existingBook.CategoryId = dto.CategoryId;

            _bookDAO.UpdateBook(existingBook);
        }

        public async Task UpdateBookPartialAsync(BookEditDto dto)
        {
            var book = await _bookDAO.GetBookByIdAsync(dto.BookID);
            if (book == null)
                throw new Exception("Book not found");

            if (!string.IsNullOrWhiteSpace(dto.Title)) book.Title = dto.Title;
            if (!string.IsNullOrWhiteSpace(dto.Description)) book.Description = dto.Description;
            if (dto.Price.HasValue) book.Price = dto.Price.Value;
            if (dto.Stock.HasValue) book.Stock = dto.Stock.Value;
            if (!string.IsNullOrWhiteSpace(dto.ImageUrl)) book.ImageUrl = dto.ImageUrl;
            if (dto.AuthorId.HasValue) book.AuthorId = dto.AuthorId.Value;
            if (dto.CategoryId.HasValue) book.CategoryId = dto.CategoryId.Value;

            _bookDAO.UpdateBook(book);
        }
        //Mapping helpers
        private BookDto MapToDTO(Book b) => new BookDto
        {
            BookID = b.BookId,
            Title = b.Title,
            Description = b.Description,
            Price = b.Price,
            Stock = b.Stock,
            ImageUrl = b.ImageUrl,
            AuthorId = b.AuthorId,
            CategoryId = b.CategoryId,
            AuthorName = b.Author.Name,
            CategoryName = b.Category.Name
        };

        private Book MapToEntity(BookDto dto) => new Book
        {
            Title = dto.Title,
            Description = dto.Description,
            Price = dto.Price,
            Stock = dto.Stock,
            ImageUrl = dto.ImageUrl,
            AuthorId = dto.AuthorId,
            CategoryId = dto.CategoryId
        };
    
}
}
