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
    assertNull(entityNameCache.get("test"));

    Entity mockEntity = Mockito.mock(Entity.class);

    Mockito.when(mockEntity.getName()).thenReturn("test");
    entityNameCache.update(mockEntity, null);
    assertEquals(mockEntity, entityNameCache.get("test"));

    Mockito.when(mockEntity.getName()).thenReturn("updated");
    entityNameCache.update(mockEntity, "test");
    assertNull(entityNameCache.get("test"));
    assertEquals(mockEntity, entityNameCache.get("updated"));

    entityNameCache.remove("updated");
    assertNull(entityNameCache.get("updated"));
  }
}
