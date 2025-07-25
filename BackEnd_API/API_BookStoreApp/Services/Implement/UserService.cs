﻿using BusinessObject.Models;
using DataAccessObject;
using DTOs.DTO;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using Services.Interface;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace Services.Implement
{
    public class UserService : IUserService
    {
        private readonly UserDAO _userDAO;
        private readonly IConfiguration _configuration;
        public UserService(UserDAO userDAO, IConfiguration configuration)
        {
            _userDAO = userDAO;
            _configuration = configuration;
        }

        //public string Authenticate(UserLoginDto loginDto)
        //{
        //    var user = _userDAO.ValidateUser(loginDto.Email, loginDto.Password);
        //    if (user == null) throw new Exception("Invalid credentials");
        //    // khởi tạo 1 đối tượng giúp xử lý JWT
        //    var tokenHandler = new JwtSecurityTokenHandler();
        //    // khóa bí mật rồi chuyển thành mảng byte
        //    var key = Encoding.ASCII.GetBytes(_configuration["Jwt:Key"]);
        //    // mô tả nội dung của 1 JWT token
        //    var tokenDescriptor = new SecurityTokenDescriptor
        //    {
        //        // một đối tượng đại diện cho danh tính người dùng
        //        Subject = new ClaimsIdentity(new[]
        //        {
        //            // 1 cliam là 1 mẩu thông tin gắn với người dùng
        //        new Claim(ClaimTypes.NameIdentifier, user.UserId.ToString()),
        //        new Claim(ClaimTypes.Name, user.Username),
        //        new Claim(ClaimTypes.Role, user.Role ?? "User"),
        //        new Claim("UserID", user.UserId.ToString()),

        //    }),
        //        Expires = DateTime.UtcNow.AddDays(1),
        //        SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
        //    };
        //    var token = tokenHandler.CreateToken(tokenDescriptor);
        //    return tokenHandler.WriteToken(token);
        //}

        public UserResponseDto Authenticate(UserLoginDto loginDto)
        {
            var user = _userDAO.ValidateUser(loginDto.Email, loginDto.Password);
            if (user == null) throw new Exception("Sai tài khoản hoặc mật khẩu");

            return new UserResponseDto
            {
                UserId = user.UserId,
                Username = user.Username,
                FullName = user.FullName,
                Email = user.Email,
                Role = user.Role
            };
        }


        public UserResponseDto GetById(int id)
        {
            var user = _userDAO.GetUserById(id);
            if (user == null) throw new Exception("User not found");

            return new UserResponseDto
            {
                UserId = user.UserId,
                Username = user.Username,
                FullName = user.FullName,
                Email = user.Email,
                Role = user.Role
            };
        }
        public void Register(UserRegisterDTO userDto)
        {
            // Optional: check if email already exists
            if (_userDAO.GetUserByEmail(userDto.Email) != null)
            {
                throw new Exception("Email already exists");
            }

            var user = new User
            {
                FullName = userDto.FullName,
                Email = userDto.Email,
                Password = userDto.Password, // Optional: hash password here
                Username=userDto.Username,
                Role = "Customer"
            };

            _userDAO.AddUser(user);
        }
        public void UpdateUser(UserUpdateDTO dto)
        {
            var user = _userDAO.GetUserById(dto.UserId);
            if (user == null)
                throw new Exception("User not found");

            // ✅ Xác minh mật khẩu hiện tại
            if (user.Password != dto.CurrentPassword)
                throw new Exception("Mật khẩu hiện tại không đúng");

            // ✅ Cập nhật username và fullname nếu có nhập
            if (!string.IsNullOrEmpty(dto.Username))
                user.Username = dto.Username;

            if (!string.IsNullOrEmpty(dto.FullName))
                user.FullName = dto.FullName;

            // ✅ Nếu có mật khẩu mới thì thay đổi
            if (!string.IsNullOrEmpty(dto.NewPassword))
                user.Password = dto.NewPassword;

            _userDAO.UpdateUser(user);
        }
    }
}
