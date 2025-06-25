using BusinessObject.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessObject
{
    public class BookDAO
    {
        private readonly BookStoreAppContext _context;
        public BookDAO(BookStoreAppContext context)
        {
            _context = context;
        }
        public async Task<List<Book>> GetAllBooksAsync()
        {
            return await _context.Books
                .Include(b => b.Author)
                .Include(b => b.Category)
                .ToListAsync();
        }
        public async Task<Book> GetBookByIdAsync(int id)
        {
            return await _context.Books
                .Include(b => b.Author)
                .Include(b => b.Category)
                .FirstOrDefaultAsync(b => b.BookId == id);
        }
        public void AddBook(Book c)
        {
            try
            {
                _context.Books.Add(c);
                _context.SaveChanges();
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
        }
        public void UpdateBook(Book c)
        {
            try
            {
                _context.Books.Update(c);
                _context.SaveChanges();
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
        }
        public void DeleteBook(Book c)
        {
            try
            {
                _context.Books.Remove(c);
                _context.SaveChanges();
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
        }
        public async Task<List<Book>> SearchBooksAsync(string keyword)
        {
            return await _context.Books
                .Include(b => b.Author)
                .Include(b => b.Category)
                .Where(b => b.Title.Contains(keyword)|| b.Description.Contains(keyword)||b.Category.Name.ToLower().Contains(keyword.ToLower())||b.Author.Name.ToLower().Contains(keyword.ToLower()))
                .ToListAsync();
        }
        public async Task<List<Book>> GetBooksByCategoryAsync(int categoryId)
        {
            return await _context.Books
                .Include(b => b.Author)
                .Include(b => b.Category)
                .Where(b => b.CategoryId == categoryId)
                .ToListAsync();
        }

    }
}
