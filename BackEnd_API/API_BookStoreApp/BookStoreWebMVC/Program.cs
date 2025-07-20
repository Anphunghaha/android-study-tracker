using BookStoreWebMVC.Models; // Thay bằng namespace thật sự của bạn nếu khác
using Microsoft.Extensions.Options;

var builder = WebApplication.CreateBuilder(args);

// Đăng ký cấu hình ApiSettings từ appsettings.json
builder.Services.Configure<ApiSettings>(builder.Configuration.GetSection("ApiSettings"));

// Đăng ký HttpClient để gọi API
builder.Services.AddHttpClient();

// Đăng ký Session
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(30); // thời gian timeout session
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
});

builder.Services.AddControllersWithViews();

var app = builder.Build();

// Cấu hình pipeline HTTP
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

// Bật session trước khi xác thực
app.UseSession();

app.UseAuthorization();

// Định tuyến mặc định: vào login trước
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Account}/{action=Login}/{id?}");
   // pattern: "{controller=Admin}/{action=Dashboard}");

app.Run();
