using DTOs.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Services.Interface
{
    public interface IAuthorService
    {
        Task<List<AuthorDto>> GetAllAsync();
        Task<AuthorDto?> GetByIdAsync(int id);
        Task AddAsync(AuthorDto dto);
        Task UpdateAsync(AuthorDto dto, int idFromRoute);
        Task DeleteAsync(int id);
    }
}
