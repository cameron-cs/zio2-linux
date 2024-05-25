package org.cameron.cs

import com.sun.jna.{Memory, Pointer}
import zio.*

object ZioLinuxIOJna {

  def close(fd: Int): Task[Int] =
    ZIO.attempt(CLib.INSTANCE.close(fd))

  def open(pathname: String, flags: Int): Task[Int] =
    ZIO.attempt(CLib.INSTANCE.open(pathname, flags))
  
  def ioctl(fd: Int, request: Long, arg: Long): Task[Int] =
    ZIO.attempt(CLib.INSTANCE.ioctl(fd, request, arg))


  def lseek(fd: Int, offset: Long, whence: Int): Task[Long] =
    ZIO.attempt(CLib.INSTANCE.lseek(fd, offset, whence))

  def lseek64(fd: Int, offset: Long, whence: Int): Task[Long] = 
    ZIO.attempt(CLib.INSTANCE.lseek64(fd, offset, whence))

  def open64(pathname: String, flags: Int): Task[Int] = 
    ZIO.attempt(CLib.INSTANCE.open64(pathname, flags))

  def openat64(dirfd: Int, pathname: String, flags: Int): Task[Int] = 
    ZIO.attempt(CLib.INSTANCE.openat64(dirfd, pathname, flags))

  def read(fd: Int, buf: Pointer, count: Long): Task[Long] = 
    ZIO.attempt(CLib.INSTANCE.read(fd, buf, count))

  def write(fd: Int, buf: Pointer, count: Long): Task[Long] = 
    ZIO.attempt(CLib.INSTANCE.write(fd, buf, count))
}
