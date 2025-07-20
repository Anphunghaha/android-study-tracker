using BusinessObject.Models;
using DataAccessObject;
using DTOs.DTO;
using Services.Interface;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Services.Implement
{
    public class AuthorService : IAuthorService
    {
        private readonly AuthorDAO _authorDAO;

        public AuthorService(AuthorDAO authorDAO)
        {
            _authorDAO = authorDAO;
        }

        public async Task<List<AuthorDto>> GetAllAsync()
        {
            var authors = await _authorDAO.GetAllAuthorsAsync();
            return authors.Select(MapToDTO).ToList();
        }

        public async Task<AuthorDto?> GetByIdAsync(int id)
        {
            var author = await _authorDAO.GetAuthorByIdAsync(id);
            return author != null ? MapToDTO(author) : null;
        }

        public async Task AddAsync(AuthorDto dto)
        {
            var author = MapToEntity(dto);
            _authorDAO.AddAuthor(author);
            await Task.CompletedTask;
        }

        public async Task UpdateAsync(AuthorDto dto, int idFromRoute)
        {
            var existing = await _authorDAO.GetAuthorByIdAsync(idFromRoute);
            if (existing == null)
                throw new Exception("Author not found.");

            existing.Name = dto.Name;
            existing.Bio = dto.Bio;
            existing.BirthDate = dto.BirthDate;
            existing.CreatedAt = dto.CreatedAt;

            _authorDAO.UpdateAuthor(existing);
            await Task.CompletedTask;
        }

        public async Task DeleteAsync(int id)
        {
            var author = await _authorDAO.GetAuthorByIdAsync(id);
            if (author != null)
            {
                _authorDAO.DeleteAuthor(author);
            }
            await Task.CompletedTask;
        }

        // Mapping
        private AuthorDto MapToDTO(Author a) => new AuthorDto
        {
            AuthorId = a.AuthorId,
            Name = a.Name,
            Bio = a.Bio,
            BirthDate = a.BirthDate,
            CreatedAt = a.CreatedAt
        };

        private Author MapToEntity(AuthorDto dto) => new Author
        {
            Name = dto.Name,
            Bio = dto.Bio,
            BirthDate = dto.BirthDate,
            CreatedAt = dto.CreatedAt
        };
    }
}
