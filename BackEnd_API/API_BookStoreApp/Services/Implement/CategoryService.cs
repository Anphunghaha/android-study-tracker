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
    public class CategoryService : ICategoryService
    {
        private readonly CategoryDAO _categoryDAO;

        public CategoryService(CategoryDAO categoryDAO)
        {
            _categoryDAO = categoryDAO;
        }

        public async Task<List<CategoryDTO>> GetAllCategoriesAsync()
        {
            var categories = await _categoryDAO.GetAllCategoriesAsync();
            return categories.Select(MapToDTO).ToList();
        }

        public async Task<CategoryDTO?> GetCategoryByIdAsync(int id)
        {
            var category = await _categoryDAO.GetCategoryByIdAsync(id);
            return category != null ? MapToDTO(category) : null;
        }

        public async Task AddCategoryAsync(CategoryDTO dto)
        {
            var category = MapToEntity(dto);
            _categoryDAO.AddCategory(category); // DAO đang là sync
            await Task.CompletedTask;
        }

        public async Task UpdateCategoryAsync(CategoryDTO dto, int idFromRoute)
        {
            var existing = await _categoryDAO.GetCategoryByIdAsync(idFromRoute);
            if (existing == null)
                throw new Exception("Category not found.");

            existing.Name = dto.Name;
            existing.Description = dto.Description;
            existing.CreatedAt = dto.CreatedAt;

            _categoryDAO.UpdateCategory(existing);
            await Task.CompletedTask;
        }

        public async Task DeleteCategoryAsync(int id)
        {
            var category = await _categoryDAO.GetCategoryByIdAsync(id);
            if (category != null)
            {
                _categoryDAO.DeleteCategory(category);
            }
            await Task.CompletedTask;
        }

        // Mapping
        private CategoryDTO MapToDTO(Category c) => new CategoryDTO
        {
            CategoryId = c.CategoryId,
            Name = c.Name,
            Description = c.Description,
            CreatedAt = c.CreatedAt
        };

        private Category MapToEntity(CategoryDTO dto) => new Category
        {
          //  CategoryId = dto.CategoryId,
            Name = dto.Name,
            Description = dto.Description,
            CreatedAt = dto.CreatedAt
        };
    }
}
