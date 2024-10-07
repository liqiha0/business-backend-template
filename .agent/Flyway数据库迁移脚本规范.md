# Flyway 数据库迁移脚本规范

为确保数据库结构演进的一致性、可追溯性和自动化，本项目所有数据库变更都必须通过 Flyway 脚本进行管理。本文档定义了相关脚本的编写规范。

## 1. 脚本位置

项目采用多模块结构，每个拥有独立数据模型的模块都应在自己的 `src/main/resources/db/` 目录下管理其迁移脚本。

- **命名约定**: 为了避免不同模块之间的脚本位置冲突，Flyway 的 `location` 应遵循 `migration_<模块名>` 的格式。
  - `core` 模块: `src/main/resources/db/migration_core/`
  - `order` 模块: `src/main/resources/db/migration_order/`
  - `app` 主模块: 可使用默认位置 `src/main/resources/db/migration/`

## 2. 文件命名

所有迁移脚本都必须遵循 Flyway 的标准命名格式：

**`V<版本号>__<描述>.sql`**

- **`V`**: 固定前缀，代表版本化迁移。
- **`<版本号>`**: 版本号采用 `主版本.次版本` 的格式，例如 `1.0`, `2.1`。
  - 新模块的第一个脚本通常是 `V1.0__init.sql`。
  - 后续的脚本应递增版本号。
- **`__` (双下划线)**: 版本号和描述之间的固定分隔符。
- **`<描述>`**: 对本次变更的简短、清晰的英文描述，单词之间使用下划线 `_` 分隔（snake_case）。例如：`create_product_table`。

## 3. SQL 编码风格

为保持脚本的可读性和一致性，请遵循以下SQL编码风格：

- **关键字**: 所有SQL关键字均使用小写，例如 `create table`, `select`, `alter table`。
- **标识符**: 表名和列名必须使用小写字母的 `snake_case` 风格，例如 `user_profile`, `display_name`, `principal_id`。
- **主键**: 实体表的主键应为 `id`，类型为 `uuid`，并设为 `primary key`。
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
- **约束**: 约束（如 `not null`, `unique`）应在列定义时直接声明。外键约束也应明确定义。

## 4. 示例

一个符合规范的 `create table` 脚本示例如下：

```sql
-- 文件名: V2.0__create_store_and_product_tables.sql

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
