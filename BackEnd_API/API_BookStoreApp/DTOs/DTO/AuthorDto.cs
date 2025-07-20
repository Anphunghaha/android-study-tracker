using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DTOs.DTO
{
    public class AuthorDto
    {
        public int? AuthorId { get; set; }
        public string Name { get; set; }
        public string? Bio { get; set; }
        public DateTime? BirthDate { get; set; }
        public DateTime? CreatedAt { get; set; }
    }
}
