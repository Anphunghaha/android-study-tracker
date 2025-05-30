USE [master]
GO
/****** Object:  Database [DBManageSubjectScheduler]    Script Date: 5/27/2025 3:20:23 PM ******/
CREATE DATABASE [DBManageSubjectScheduler]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'DBManageSubjectScheduler', FILENAME = N'D:\SQL\MSSQL16.SQLEXPRESS\MSSQL\DATA\DBManageSubjectScheduler.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'DBManageSubjectScheduler_log', FILENAME = N'D:\SQL\MSSQL16.SQLEXPRESS\MSSQL\DATA\DBManageSubjectScheduler_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [DBManageSubjectScheduler] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [DBManageSubjectScheduler].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [DBManageSubjectScheduler] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET ARITHABORT OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET  ENABLE_BROKER 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET  MULTI_USER 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [DBManageSubjectScheduler] SET DB_CHAINING OFF 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [DBManageSubjectScheduler] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [DBManageSubjectScheduler] SET QUERY_STORE = ON
GO
ALTER DATABASE [DBManageSubjectScheduler] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [DBManageSubjectScheduler]
GO
/****** Object:  Table [dbo].[Subjects]    Script Date: 5/27/2025 3:20:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Subjects](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[UserId] [int] NULL,
	[Name] [nvarchar](100) NULL,
	[Lecturer] [nvarchar](100) NULL,
	[Schedule] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Tasks]    Script Date: 5/27/2025 3:20:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Tasks](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[SubjectId] [int] NULL,
	[Title] [nvarchar](100) NULL,
	[Description] [nvarchar](max) NULL,
	[Deadline] [date] NULL,
	[IsDone] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 5/27/2025 3:20:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](100) NULL,
	[Email] [nvarchar](100) NULL,
	[Password] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Subjects] ON 

INSERT [dbo].[Subjects] ([Id], [UserId], [Name], [Lecturer], [Schedule]) VALUES (1, 1, N'Lập trình Java', N'Trần Văn C', N'T2,T4')
INSERT [dbo].[Subjects] ([Id], [UserId], [Name], [Lecturer], [Schedule]) VALUES (2, 1, N'Cơ sở dữ liệu', N'Nguyễn Thị D', N'T3,T5')
INSERT [dbo].[Subjects] ([Id], [UserId], [Name], [Lecturer], [Schedule]) VALUES (3, 2, N'Thiết kế giao diện', N'Lê Văn E', N'T2,T6')
SET IDENTITY_INSERT [dbo].[Subjects] OFF
GO
SET IDENTITY_INSERT [dbo].[Tasks] ON 

INSERT [dbo].[Tasks] ([Id], [SubjectId], [Title], [Description], [Deadline], [IsDone]) VALUES (1, 1, N'Bài 1: Hello World', N'Viết chương trình Hello World bằng Java', CAST(N'2025-06-01' AS Date), 0)
INSERT [dbo].[Tasks] ([Id], [SubjectId], [Title], [Description], [Deadline], [IsDone]) VALUES (2, 1, N'Bài 2: Mảng 1 chiều', N'Viết chương trình thao tác mảng', CAST(N'2025-06-05' AS Date), 1)
INSERT [dbo].[Tasks] ([Id], [SubjectId], [Title], [Description], [Deadline], [IsDone]) VALUES (3, 2, N'Bài 1: Thiết kế bảng', N'Tạo bảng sinh viên và khóa ngoại', CAST(N'2025-06-02' AS Date), 0)
INSERT [dbo].[Tasks] ([Id], [SubjectId], [Title], [Description], [Deadline], [IsDone]) VALUES (4, 3, N'Bài 1: Vẽ giao diện login', N'Thiết kế layout màn hình login bằng XML', CAST(N'2025-06-03' AS Date), 0)
SET IDENTITY_INSERT [dbo].[Tasks] OFF
GO
SET IDENTITY_INSERT [dbo].[Users] ON 

INSERT [dbo].[Users] ([Id], [Name], [Email], [Password]) VALUES (1, N'Nguyễn Văn A', N'a@example.com', N'123456')
INSERT [dbo].[Users] ([Id], [Name], [Email], [Password]) VALUES (2, N'Lê Thị B', N'b@example.com', N'123456')
SET IDENTITY_INSERT [dbo].[Users] OFF
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Users__A9D1053455302A2C]    Script Date: 5/27/2025 3:20:23 PM ******/
ALTER TABLE [dbo].[Users] ADD UNIQUE NONCLUSTERED 
(
	[Email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Tasks] ADD  DEFAULT ((0)) FOR [IsDone]
GO
ALTER TABLE [dbo].[Subjects]  WITH CHECK ADD FOREIGN KEY([UserId])
REFERENCES [dbo].[Users] ([Id])
GO
ALTER TABLE [dbo].[Tasks]  WITH CHECK ADD FOREIGN KEY([SubjectId])
REFERENCES [dbo].[Subjects] ([Id])
GO
USE [master]
GO
ALTER DATABASE [DBManageSubjectScheduler] SET  READ_WRITE 
GO
