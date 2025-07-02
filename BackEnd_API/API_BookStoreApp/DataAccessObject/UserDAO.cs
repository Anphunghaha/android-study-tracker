using BusinessObject.Models;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessObject
{
    public class UserDAO
    {
        private readonly BookStoreAppContext _context;

        public UserDAO(BookStoreAppContext context)
        {
            _context = context;
        }

        public User? ValidateUser(string email, string password)
        {
            return _context.Users.FirstOrDefault(u => u.Email.ToLower().Equals(email) && u.Password.Equals(password));
        }

        public User? GetUserById(int id)
        {
            return _context.Users.Find(id);
        }

        public void AddUser(User user)
        {
            _context.Users.Add(user);
            _context.SaveChanges();
        }

        public User? GetUserByEmail(string email)
        {
         return _context.Users.FirstOrDefault(d => d.Email.ToLower().Equals(email.ToLower()));
        }
    }

}
