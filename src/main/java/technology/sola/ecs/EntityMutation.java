package technology.sola.ecs;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.cache.EntityNameCache;
import technology.sola.ecs.cache.ViewCache;

@NullMarked
interface EntityMutation {
  void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache);

  record Created(
    int entityIndex,
    @Nullable String name,
    Component[] initialComponents
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      var entity = world.getEntityAtIndex(entityIndex);

      if (entity != null) {
        entity.setDisabledImmediately(false);

        // update the name cache if a name was set
        if (name != null) {
          entity.setNameImmediately(name);
          entityNameCache.update(entity, null);
        }

        // update components
        for (Component component : initialComponents) {
          world.addComponent(entityIndex, component);
        }
      }
    }
  }

  record Name(
    int entityIndex,
    @Nullable String name
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      var entity = world.getEntityAtIndex(entityIndex);

      if (entity != null) {
        var previousName = entity.getName();

        entity.setNameImmediately(name);
        entityNameCache.update(entity, previousName);
      }
    }
  }

  record Disable(
    int entityIndex,
    boolean isDisabled
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      var entity = world.getEntityAtIndex(entityIndex);

      if (entity != null) {
        entity.setDisabledImmediately(isDisabled);
        viewCache.updateForDisabledStateChange(entity);
      }
    }
  }

  record Destroy(
    int entityIndex
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      var entity = world.getEntityAtIndex(entityIndex);

      if (entity != null) {
        world.destroy(entityIndex);
        entityNameCache.remove(entity.getName());
        viewCache.updateForDeletedEntity(entity);
      }
    }
  }

  record AddComponent(
    int entityIndex,
    Component component
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      world.addComponent(entityIndex, component);
    }
  }

  record RemoveComponent(
    int entityIndex,
    Class<? extends Component> componentClass
  ) implements EntityMutation {
    @Override
    public void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
      world.removeComponent(entityIndex, componentClass, true);
    }
  }
}
