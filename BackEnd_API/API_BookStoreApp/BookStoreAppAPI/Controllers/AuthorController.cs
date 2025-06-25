using DTOs.DTO;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Services.Interface;

namespace BookStoreAppAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthorController : ControllerBase
    {
        private readonly IAuthorService _authorService;

        public AuthorController(IAuthorService authorService)
        {
            _authorService = authorService;
        }

        // GET: api/Author
        [HttpGet]
        public async Task<ActionResult<List<AuthorDto>>> GetAll()
        {
            var authors = await _authorService.GetAllAsync();
            return Ok(authors);
        }

        // GET: api/Author/5
        [HttpGet("{id}")]
        public async Task<ActionResult<AuthorDto>> GetById(int id)
        {
            var author = await _authorService.GetByIdAsync(id);
            if (author == null) return NotFound();
            return Ok(author);
        }

        // POST: api/Author
        [HttpPost]
        public async Task<IActionResult> Create([FromBody] AuthorDto dto)
        {
            await _authorService.AddAsync(dto);
            return StatusCode(201); // Created
        }

        // PUT: api/Author/5
        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, [FromBody] AuthorDto dto)
        {
            if (id != dto.AuthorId)
                return BadRequest("ID mismatch between route and body.");

            await _authorService.UpdateAsync(dto, id);
            return NoContent(); // 204
        }

        // DELETE: api/Author/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            await _authorService.DeleteAsync(id);
            return NoContent();
        }
    }
}

