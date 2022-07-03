package technology.sola.ecs.io;

import technology.sola.ecs.World;
import technology.sola.ecs.exception.Base64WorldIoException;

import java.io.*;
import java.util.Base64;

/**
 * A Base64 implementation of {@link WorldIo}.
 */
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
      throw new Base64WorldIoException(ex);
    }
  }

  @Override
  public World parse(String worldString) {
    final byte[] data = Base64.getDecoder().decode(worldString);

    try (final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
      World world = (World) ois.readObject();

      WorldIo.processWorldAfterDeserialize(world);

      return world;
    } catch (IOException ex) {
      throw new Base64WorldIoException(ex);
    } catch (ClassNotFoundException ex) {
      throw new Base64WorldIoException(ex);
    }
  }
}
