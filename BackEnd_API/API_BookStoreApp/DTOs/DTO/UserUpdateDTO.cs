using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DTOs.DTO
{
    public class UserUpdateDTO
    {
        public int UserId { get; set; }

        public string? Username { get; set; }

        public string? FullName { get; set; }

        public string? NewPassword { get; set; } // Nếu người dùng muốn đổi mật khẩu

        public string CurrentPassword { get; set; } = null!; 
    }
}
