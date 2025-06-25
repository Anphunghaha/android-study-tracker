using DataAccessObject;
using DTOs.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Services.Interface
{
    public interface ICategoryService
    {
        Task<List<CategoryDTO>> GetAllCategoriesAsync();
        Task<CategoryDTO?> GetCategoryByIdAsync(int id);
        Task AddCategoryAsync(CategoryDTO dto);
        Task UpdateCategoryAsync(CategoryDTO dto, int idFromRoute);
        Task DeleteCategoryAsync(int id);
    }
}
