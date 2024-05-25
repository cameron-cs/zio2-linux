package org.cameron.cs

import com.sun.jna.Pointer
import zio._

trait LinuxIOService {

  def close(fd: Int): Task[Int]

  def open(pathname: String, flags: Int): Task[Int]

  def ioctl(fd: Int, request: Long, arg: Long): Task[Int]

  def lseek(fd: Int, offset: Long, whence: Int): Task[Long]

  def lseek64(fd: Int, offset: Long, whence: Int): Task[Long]

  def open64(pathname: String, flags: Int): Task[Int]

  def openat64(dirfd: Int, pathname: String, flags: Int): Task[Int]

  def read(fd: Int, buf: Pointer, count: Long): Task[Long]

  def write(fd: Int, buf: Pointer, count: Long): Task[Long]
}

object LinuxIOService {

  def close(fd: Int): ZIO[LinuxIOService, Throwable, Int] =
    ZIO.serviceWithZIO[LinuxIOService](_.close(fd))

  def open(pathname: String, flags: Int): ZIO[LinuxIOService, Throwable, Int] =
    ZIO.serviceWithZIO[LinuxIOService](_.open(pathname, flags))

  def ioctl(fd: Int, request: Long, arg: Long): ZIO[LinuxIOService, Throwable, Int] =
    ZIO.serviceWithZIO[LinuxIOService](_.ioctl(fd, request, arg))

  def lseek(fd: Int, offset: Long, whence: Int): ZIO[LinuxIOService, Throwable, Long] =
    ZIO.serviceWithZIO[LinuxIOService](_.lseek(fd, offset, whence))

  def lseek64(fd: Int, offset: Long, whence: Int): ZIO[LinuxIOService, Throwable, Long] =
    ZIO.serviceWithZIO[LinuxIOService](_.lseek64(fd, offset, whence))

  def open64(pathname: String, flags: Int): ZIO[LinuxIOService, Throwable, Int] =
    ZIO.serviceWithZIO[LinuxIOService](_.open64(pathname, flags))

  def openat64(dirfd: Int, pathname: String, flags: Int): ZIO[LinuxIOService, Throwable, Int] =
    ZIO.serviceWithZIO[LinuxIOService](_.openat64(dirfd, pathname, flags))

  def read(fd: Int, buf: Pointer, count: Long): ZIO[LinuxIOService, Throwable, Long] =
    ZIO.serviceWithZIO[LinuxIOService](_.read(fd, buf, count))

  def write(fd: Int, buf: Pointer, count: Long): ZIO[LinuxIOService, Throwable, Long] =
    ZIO.serviceWithZIO[LinuxIOService](_.write(fd, buf, count))

  val live: ULayer[LinuxIOService] = ZLayer.succeed {
    new LinuxIOService {

      override def close(fd: Int): Task[Int] = ZioLinuxIOJna.close(fd)

      override def open(pathname: String, flags: Int): Task[Int] = ZIO.attempt {
        CLib.INSTANCE.open(pathname, flags)
      }

      override def ioctl(fd: Int, request: Long, arg: Long): Task[Int] = ZIO.attempt {
        CLib.INSTANCE.ioctl(fd, request, arg)
      }

      def lseek(fd: Int, offset: Long, whence: Int): Task[Long] =
        ZIO.attempt(CLib.INSTANCE.lseek(fd, offset, whence))

      override def lseek64(fd: Int, offset: Long, whence: Int): Task[Long] = ZIO.attempt {
        CLib.INSTANCE.lseek64(fd, offset, whence)
      }

      override def open64(pathname: String, flags: Int): Task[Int] = ZIO.attempt {
        CLib.INSTANCE.open64(pathname, flags)
      }

      override def openat64(dirfd: Int, pathname: String, flags: Int): Task[Int] = ZIO.attempt {
        CLib.INSTANCE.openat64(dirfd, pathname, flags)
      }

      override def read(fd: Int, buf: Pointer, count: Long): Task[Long] = ZIO.attempt {
        CLib.INSTANCE.read(fd, buf, count)
      }

      override def write(fd: Int, buf: Pointer, count: Long): Task[Long] = ZIO.attempt {
        CLib.INSTANCE.write(fd, buf, count)
      }
    }
  }

  object Mode {
    val O_APPEND = 1024
    val O_ASYNC = 8192
    val O_CLOEXEC = 524288
    val O_CREAT = 64
    val O_DIRECT = 16384
    val O_DIRECTORY = 65536
    val O_DSYNC = 4096
    val O_EXCL = 128
    val O_LARGEFILE = 0
    val O_NOATIME = 262144
    val O_NONBLOCK = 2048
    val O_NOCTTY = 256
    val O_NOFOLLOW = 131072
    val O_PATH = 2097152
    val O_RDONLY = 0
    val O_RDWR = 2
    val O_SYNC = 1052672
    val O_TMPFILE = 4259840
    val O_TRUNC = 512
    val O_WRONLY = 1
  }

  object Permission {
    val S_IRWXU = 448
    val S_IRUSR = 256
    val S_IWUSR = 128
    val S_IXUSR = 64
    val S_IRWXG = 56
    val S_IRGRP = 32
    val S_IWGRP = 16
    val S_IXGRP = 8
    val S_IRWXO = 7
    val S_IROTH = 4
    val S_IWOTH = 2
    val S_IXOTH = 1
  }

  object Indicator {
    val SEEK_SET = 0
    val SEEK_CUR = 1
    val SEEK_END = 2
  }
}