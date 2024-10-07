## 项目概述

本项目是一个基于 Gradle 构建的多模块 Spring Boot 应用，遵循领域驱动设计（DDD）分层架构思想。项目被划分为多个独立的模块，以实现高内-低耦合的设计。

### 模块概览

以下是项目的主要模块及其职责：

- **`app`**: 主应用模块，是整个项目的入口和核心业务实现。它整合了其他所有模块，并提供了API接口。
- **`core`**: 核心功能模块，提供了整个应用共享的基础服务。
- **`order`**: 订单模块，负责处理所有与订单、支付相关的业务逻辑。
- **`storage`**: 存储模块，提供文件或其他对象的存储服务。
- **`notification`**: 通知模块，负责发送各种类型的通知。
- **`verification`**: 验证模块，提供验证码等验证服务。

### 单个模块的内部结构

每个独立的业务模块（如 `order`, `storage` 等）都遵循统一的目录结构：

- `application/`: 应用服务层
- `domain/`: 领域层
  - `model/`: 存放聚合根、实体、值对象等领域模型
  - `service/`: 存放领域服务，处理跨聚合的业务逻辑
  - `repository/`: 领域仓库的接口定义
- `infrastructure/`: 基础设施层
  - `config/`: 模块相关的Spring配置
- `interfaces/`: 接口层
  - `controller/`: API 控制器，暴露HTTP接口。

---

## 聚合根（Aggregate Root）核心实现指南

本文档为项目提供统一的、核心的聚合根（Aggregate Root）代码实现标准。所有新功能开发与重构都应遵循此指南。

### 继承 `AuditableAggregateRoot`

所有聚合根 **必须** 继承 `core` 模块提供的 `AuditableAggregateRoot`。

- **目的**:
    1. **自动审计**: 自动获取并管理 `createdBy`, `createdDate`, `lastModifiedBy`, `lastModifiedDate` 字段。
    2. **领域事件**: 获得 `registerEvent()` 方法，用于发布领域事件，实现模块解耦。

#### 聚合根的结构 (JPA 兼容)

- **`protected` 属性**: 为了兼容 Hibernate/JPA 的代理机制（如懒加载），内部状态应设为 `protected` 或使用 `protected set`
  ，而不是 `private`。
- **主构造函数**: 用于创建聚合，并执行创建时的业务规则验证。

**范例：一个通用的聚合根结构**

```kotlin
import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
open class YourAggregate(
    initialName: String,

    ) : AuditableAggregateRoot<YourAggregate>() {

    init {
        require(initialName.isNotBlank())
    }

    @Id
    @UuidGenerator
    lateinit var id: UUID
        protected set

    var name: String = initialName
        protected set(value) {
          require(value.isNotBlank())
        }
}
```

#### 管理子实体集合

如果聚合根包含子实体集合，**绝不能** 将可变的集合暴露给外部。

**范例：`Order` 与 `OrderItem`**

```kotlin
open class Order : AuditableAggregateRoot<Order>() {
    // ...
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val _orderItems = mutableListOf<OrderItem>()

    val orderItems: List<OrderItem>
        get() = _orderItems.toList()

    fun addItem(product: Product, quantity: Int) {
        val newItem = OrderItem(product.id, quantity, product.price)
        _orderItems.add(newItem)
    }
}
```

### 使用领域事件进行通信

当聚合根发生重要状态变更，需要通知系统其他部分时，应发布领域事件。在业务方法内部调用 `registerEvent(YourEvent(...))` 即可。

---

## Flyway 数据库迁移脚本规范

为确保数据库结构演进的一致性、可追溯性和自动化，本项目所有数据库变更都必须通过 Flyway 脚本进行管理。本文档定义了相关脚本的编写规范。

### 脚本位置

项目采用多模块结构，每个拥有独立数据模型的模块都应在自己的 `src/main/resources/db/` 目录下管理其迁移脚本。

- **命名约定**: 为了避免不同模块之间的脚本位置冲突，Flyway 的 `location` 应遵循 `migration_<模块名>` 的格式。
  - `core` 模块: `src/main/resources/db/migration_core/`
  - `order` 模块: `src/main/resources/db/migration_order/`
  - `app` 主模块: 使用默认位置 `src/main/resources/db/migration/`

### SQL 编码风格

为保持脚本的可读性和一致性，请遵循以下SQL编码风格：

- **关键字**: 所有SQL关键字均使用小写，例如 `create table`, `select`, `alter table`。
- **标识符**: 表名和列名必须使用小写字母的 `snake_case` 风格，例如 `display_name`, `principal_id`。
- **主键**: 通常实体表的主键为 `id`，类型为 `uuid`，并设为 `primary key`。
- **审计字段**: 所有代表聚合根或核心实体的表，都 **必须** 包含标准的审计字段，以对接 `AuditableAggregateRoot`。

    ```sql
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    ```

- **数据类型**:
  - **字符串**: 使用 `text`。
  - **时间戳**: 使用 `timestamp with time zone`。
  - **布尔值**: 使用 `boolean`。
  - **JSON**: 使用 `jsonb`。
  - **精确数字（如金额）**: 使用 `numeric(precision, scale)`，例如 `numeric(19, 2)`。
- **约束**: 约束（如 `not null`, `unique`）应在表定义时直接声明。外键约束也应明确定义。

### 示例

一个符合规范的 `create table` 脚本示例如下：

```sql
-- 文件名: V2__create_store_and_product_tables.sql

create table store
(
    id                 uuid primary key,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    principal_id       uuid                     not null,
    display_name       text                     not null,
    address            text                     not null,
    service_tags       jsonb                    not null
);

create table product
(
    id                 uuid primary key,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    store_id           uuid                     not null references store(id),
    display_name       text                     not null,
    price              numeric(19, 2)           not null,
    description        text
);
```
