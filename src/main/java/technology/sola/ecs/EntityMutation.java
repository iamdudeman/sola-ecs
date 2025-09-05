package technology.sola.ecs;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.cache.EntityNameCache;
import technology.sola.ecs.cache.ViewCache;

@NullMarked
interface EntityMutation {
  void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache);

  record Created(
    @Nullable String name,
    @Nullable String uniqueId,
    Component[] initialComponents
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      // todo

      // todo call new methods named "setDisabledImmediately" and "setNameImmediately" etc.


      // when creating maybe start it as disabled so it reserves its spot but not processed
      //   set disabled false?
    }
  }

  record Name(
    int entityId,
    @Nullable String name
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      var entity = world.getEntityAtIndex(entityId);

      if (entity != null) {
        var previousName = entity.getName();

        entity.setNameImmediately(name);
        entityNameCache.update(entity, previousName);
      }
    }
  }

  record Disable(
    int entityId,
    boolean isDisabled
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      var entity = world.getEntityAtIndex(entityId);

      if (entity != null) {
        entity.setDisabledImmediately(isDisabled);
        viewCache.updateForDisabledStateChange(entity);
      }
    }
  }

  record Destroy(
    int entityId
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      var entity = world.getEntityAtIndex(entityId);

      if (entity != null) {
        world.destroyEntity(entity);
        entityNameCache.remove(entity.getName());
        viewCache.updateForDeletedEntity(entity);
      }
    }
  }

  record AddComponent(
    int entityId,
    Component component
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      world.addComponentForEntity(entityId, component);
    }
  }

  record RemoveComponent(
    int entityId,
    Class<? extends Component> componentClass
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      world.removeComponent(entityId, componentClass, true);
    }
  }
}
