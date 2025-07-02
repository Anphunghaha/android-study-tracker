using DataAccessObject;
using DTOs.DTO;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Services.Implement;
using Services.Interface;

namespace BookStoreAppAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]

    public class BooksController : ControllerBase
    {
        private readonly IBookService _bookService;
        public BooksController(IBookService bookService)
        {
            _bookService = bookService;
        }
        [HttpGet]
        public async Task<IActionResult> GetAllBooks()
        {
            var books = await _bookService.GetAllBooksAsync();
            return Ok(books);
        }
        // GET: api/book/{id}
        [HttpGet("{id}")]
        public async Task<ActionResult<BookDto>> GetBookById(int id)
        {
            var book = await _bookService.GetBookByIdAsync(id);
            if (book == null) return NotFound();
            return Ok(book);
        }
        // GET: api/book/search?keyword=sql
        [HttpGet("search")]
        [AllowAnonymous]
        public async Task<ActionResult<List<BookDto>>> SearchBooks([FromQuery] string keyword)
        {
            var books = await _bookService.SearchBooksAsync(keyword);
            return Ok(books);
        }
        // GET: api/book/category/2
        [HttpGet("BookBycategory/{categoryId}")]
        [AllowAnonymous]
        public async Task<ActionResult<List<BookDto>>> GetBooksByCategory(int categoryId)
        {
            var books = await _bookService.GetBooksByCategoryAsync(categoryId);
            return Ok(books);
        }
        // POST: api/book
        [HttpPost]
        public async Task<ActionResult> AddBook([FromBody] BookDto book)
        {
            try
            {
                await _bookService.AddBookAsync(book);
                return Ok(new { message = "Book added successfully" });
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
        [HttpPut("{id}")]
        public async Task<ActionResult> UpdateBook(int id, [FromBody] BookDto book)
        {
            if (id != book.BookID)
                return BadRequest("Book ID mismatch.");

            try
            {
                await _bookService.UpdateBookAsync(book,id);
                return Ok(new { message = "Book updated successfully" });
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
        // DELETE: api/book/{id}
        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteBook(int id)
        {
            try
            {
                await _bookService.DeleteBookAsync(id);
                return Ok(new { message = "Book deleted successfully" });
            }
            catch (Exception ex)
            {
                return NotFound(ex.Message);
            }
        }
    }
}
