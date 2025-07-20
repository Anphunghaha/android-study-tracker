namespace BookStoreWebMVC.Models
{
    public class UserResponse
    {
        public int UserId { get; set; }
        public string? Username { get; set; }
        public string? FullName { get; set; }
        public string Email { get; set; }
        public string? Role { get; set; }
    }
}
