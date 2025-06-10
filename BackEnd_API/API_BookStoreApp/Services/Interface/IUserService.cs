using DTOs.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Services.Interface
{
    public interface IUserService
    {
        string Authenticate(UserLoginDto loginDto);
        UserResponseDto GetById(int id);
    }
}
