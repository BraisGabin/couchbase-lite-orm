# Roadmap
The aim of this library is avoid the boilerplate you need to write to transform a Couchbase Lite's `Document` to a domain `Object`.

## Possible approaches
- Couchbase Lite use Jackson under hood. Let's use the power of [Jackson Annotations](j-annotations) and [Jackson DataBind](j-databind).
- Implement an external library.

The first option can be faster but I think that it implies big changes in the API and in the internal of Couchbase Lite. The team are busy and [this is not prioritary](orm-no-prioritary).

If I'll implement an external library I need choose between:

One of this two options:
- Extending classes / Implementing interfaces
- POJO

And one of this two options:
- Reflexion
- Code generation

I prefer use POJO classes and code generation. The POJOs are more developer friendly than extending classes or implementing interfaces.
Reflexion's *too* slow and is a kind of arcane magic.

## API
- The developer must annotate the domain classes with `@Example("typeName")`
 - Any idea for the name of this annotation?
 - The `typeName` is the value of `type` in the `Document`.
  - This is a limitation because the framework doesn't force you to have a property named `type`.
- The developer must annotate the fields that they want to map with `@ExampleField("fieldName")`
 - Any idea for the name of this annotation?
 - Unannotated fields are ignored.
- The developer calls `DomainObject object = CouchbaseLiteORM.get(document);` and the object will be returned.
- The developer calls `CouchbaseLiteORM.get(domainObject, document);` and the `Document`'ll be updated (or created).

## Features
- Embeded objects will be parsed too.
- Referenced object can be loaded with an eager approach or with a Lazy approach.
 - If you use directly the name of the class it'll be eager but if you use `Lazy<OtherDomainObject>` it'll be lazy.

## Not defined yet
- Conflicts. How to resolve them.

 [j-annotations]: https://github.com/FasterXML/jackson-annotations
 [j-databind]: https://github.com/FasterXML/jackson-databind
 [orm-no-prioritary]: https://forums.couchbase.com/t/is-there-any-orm-for-android/2501/2?u=brais_gabin
