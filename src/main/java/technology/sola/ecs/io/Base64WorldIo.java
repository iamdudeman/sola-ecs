package technology.sola.ecs.io;

import technology.sola.ecs.World;

import java.io.*;
import java.util.Base64;

public class Base64WorldIo implements WorldIo {
  @Override
  public String stringify(World world) {
    try (
      final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
    ) {
      objectOutputStream.writeObject(world);

      return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    } catch (IOException ex) {
      // TODO handle this better
      throw new RuntimeException(ex);
    }
  }

  @Override
  public World parse(String worldString) {
    final byte[] data = Base64.getDecoder().decode(worldString);

    try (final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
      return (World) ois.readObject();
    } catch (IOException | ClassNotFoundException ex) {
      // TODO handle this better
      throw new RuntimeException(ex);
    }
  }
}
