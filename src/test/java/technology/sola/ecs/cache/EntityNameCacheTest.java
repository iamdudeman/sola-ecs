package technology.sola.ecs.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.ecs.Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EntityNameCacheTest {
  private EntityNameCache entityNameCache;

  @BeforeEach
  void setup() {
    entityNameCache = new EntityNameCache();
  }

  @Test
  void fullTest() {
    Entity mockEntity = Mockito.mock(Entity.class);
    Mockito.when(mockEntity.getName()).thenReturn("test");

    assertNull(entityNameCache.get("test"));

    entityNameCache.add(mockEntity);
    assertEquals(mockEntity, entityNameCache.get("test"));

    entityNameCache.remove(mockEntity);
    assertNull(entityNameCache.get("test"));
  }
}
