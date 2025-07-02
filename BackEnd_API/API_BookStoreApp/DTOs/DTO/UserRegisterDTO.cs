using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DTOs.DTO
{
    public class UserRegisterDTO
    {
        public string FullName { get; set; }

        [Required, EmailAddress]
        public string Email { get; set; }
        
        [Required]
        public string Password { get; set; }

        public string Username { get; set; }

        [Required]
        [Compare("Password", ErrorMessage = "Mật khẩu xác nhận không khớp")]
        public string ConfirmPassword { get; set; }
    }
}
