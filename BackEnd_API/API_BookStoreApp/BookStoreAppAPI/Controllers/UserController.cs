using DTOs.DTO;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Services.Interface;

namespace BookStoreAppAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly IUserService _userService;

        public UserController(IUserService userService)
        {
            _userService = userService;
        }

        [HttpPost("login")]
        public IActionResult Login([FromBody] UserLoginDto loginDto)
        {
            try
            {
                var user = _userService.Authenticate(loginDto);
                return Ok(user); // trả về thông tin user dạng DTO
            }
            catch (Exception ex)
            {
                return Unauthorized(new { message = ex.Message });
            }
        }


        [HttpGet("{id}")]
        public IActionResult GetUser(int id)
        {
            var user = _userService.GetById(id);
            return Ok(user);
        }

        [HttpPost("register")]
        public IActionResult Register([FromBody] UserRegisterDTO userDto)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState); // lỗi xác nhận sẽ nằm ở đây
            }

            _userService.Register(userDto); // chỉ cần dùng Password, bỏ qua ConfirmPassword
            return Ok(new { message = "Đăng ký thành công" });
        }

    }
}
