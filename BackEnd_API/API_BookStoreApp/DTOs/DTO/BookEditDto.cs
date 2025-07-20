﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DTOs.DTO
{
    public class BookEditDto
    {
        public int BookID { get; set; }

        public string? Title { get; set; }

        public string? Description { get; set; }

        public decimal? Price { get; set; }

        public int? Stock { get; set; }

        public string? ImageUrl { get; set; }

        public int? AuthorId { get; set; }

        public int? CategoryId { get; set; }
    }
}
