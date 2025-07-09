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
        UserResponseDto Authenticate(UserLoginDto loginDto);
        UserResponseDto GetById(int id);
        void Register(UserRegisterDTO userDto);
        void UpdateUser(UserUpdateDTO dto);

    }
}
