package org.springframework.hateoas.mediatype.collectionjson;

public abstract class _FactoryProvider {
  public static CollectionJsonMediaTypeConfigurationProvider collectionJsonMediaTypeConfigurationProvider(
      ) {
    return new CollectionJsonMediaTypeConfigurationProvider();
  }

  public static CollectionJsonAffordanceModelFactory collectionJsonAffordanceModelFactory() {
    return new CollectionJsonAffordanceModelFactory();
  }
}
