<!--
Sync Impact Report
- Version: unversioned -> 1.0.0
- Modified principles:
  - Session Startup + Architecture Rules -> 模块归属与架构边界
  - Architecture Rules + Backend -> 显式服务契约
  - Configuration And Runtime -> 配置与环境一致性
  - Testing And Verification + Backend -> 按风险分层验证
  - Change Discipline + Technical Judgment -> 简单且可追溯的变更
- Added sections: 规范来源；变更与验证流程；完整治理与语义化版本规则
- Removed sections: 独立 Backend 子节；AI 工具、编码、Git 与回复格式规则
- Migrated details:
  - ✅ AGENTS.md（AI 会话与变更操作规则）
  - ✅ docs/engineering/testing.md（测试层级、范围与命令）
  - ✅ docs/engineering/runtime-configuration.md（配置来源与环境一致性）
- Template sync:
  - ✅ .specify/templates/plan-template.md
  - ✅ .specify/templates/spec-template.md
  - ✅ .specify/templates/tasks-template.md
  - ✅ .specify/memory/constitution.md
  - ✅ .agents/skills/speckit-tasks/SKILL.md
- Deferred TODOs: none
-->
# Bookstore 项目宪章

## 核心原则

### I. 模块归属与架构边界

每项变更 MUST 明确受影响模块及职责归属。业务规则 MUST 留在对应业务模块；
`foundation` 只承载与具体业务无关且确有多个模块复用的能力。模块内代码 MUST 遵循该模块
`README.md` 声明的分层、包结构与依赖方向；架构边界发生变化时，代码与模块文档 MUST 同步修改。

理由：明确的所有权和依赖方向可防止业务能力泄漏到共享库、网关或基础设施模块。

### II. 显式服务契约

模块之间的 HTTP、消息和共享 API 交互 MUST 通过显式契约完成，不得以共享持久化实现或跨服务复用
领域对象形成隐式耦合。浏览器端对后端业务能力的访问 MUST 以 `gateway` 为统一入口；`gateway`
只承担路由、安全和横切职责。契约变更 MUST 识别提供方与消费方，并说明兼容性和迁移方式。

理由：Bookstore 是多模块微服务项目，显式边界使服务能够独立演进并可靠验证协作行为。

### III. 配置与环境一致性

运行时行为 MUST 同时考虑模块本地配置和 Config Server 中的环境配置。涉及数据源、RabbitMQ、
Eureka、Gateway 路由、端口或安全设置的变更，MUST 检查所有受支持环境；`local` 与 `docker`
配置 MUST 保持相同的业务语义，任何有意差异都必须记录。运行时依赖迁移不得只在测试配置中完成。

理由：配置是系统行为的一部分；只修改单一来源会产生测试通过但实际环境不可用的缺陷。

### IV. 按风险分层验证

行为变更 MUST 在能够证明该行为的最低有效层级获得自动化验证：纯业务规则使用单元测试，
持久化、REST、安全、启动和配置装配使用集成测试，跨服务 HTTP 或消息边界使用契约测试。
契约变化 MUST 验证受影响的提供方和消费方。无法合理自动化的验证 MUST 在计划或变更说明中记录
原因、替代检查和剩余风险。

理由：分层验证在保持反馈速度的同时覆盖 Bookstore 最容易失效的模块和运行时边界。

### V. 简单且可追溯的变更

实现 MUST 优先复用仓库内已有模式，并限制在满足已确认需求所需的最小范围；新增抽象、共享依赖或
跨模块耦合 MUST 有当前用例依据。规格、计划、任务、代码和文档 MUST 对同一变更保持一致；项目结构、
命令、配置、契约或模块规则发生变化时，负责描述它们的规范来源 MUST 同步更新。

理由：这是用于技术学习的 Demo 项目，清晰的取舍和可回溯证据比预设未来需求的复杂设计更有价值。

## 规范来源

- 本宪章定义不可被功能规格、实现计划或局部惯例放宽的工程原则。
- 根目录 `README.md` 定义项目结构、构建命令和运行入口。
- 各模块 `README.md` 定义模块内架构与模块特有约束。
- `docs/engineering/testing.md` 与 `docs/engineering/runtime-configuration.md` 定义可执行细则。

下层文档可以细化上层规则，但不得与本宪章冲突。实现现状不能单独作为偏离规范来源的依据。

## 变更与验证流程

1. 规格或计划 MUST 标明受影响模块、外部契约、运行配置、兼容性和验证范围；无影响时明确记为
   `N/A`。
2. 实现前和设计完成后 MUST 执行 Constitution Check。违反原则的方案必须先调整；确需改变原则时，
   必须先按治理流程修订宪章。
3. 交付前 MUST 完成与风险匹配的测试和静态检查，并同步受影响的规范来源；未执行项及原因必须记录。

## 治理

本宪章在工程规则上高于 Bookstore 的功能规格、计划、任务和局部约定。代码审查与 Spec Kit
工作流 MUST 验证宪章合规性；无法证明合规的变更不得视为完成。

宪章修订 MUST 说明动机、兼容性影响和必要的迁移方式，同时更新顶部 Sync Impact Report、相关模板与
规范引用。版本采用语义化版本：原则删除或不兼容重定义提升 MAJOR；新增原则或实质扩展治理要求提升
MINOR；不改变约束含义的澄清提升 PATCH。

**Version**: 1.0.0 | **Ratified**: 2026-07-15 | **Last Amended**: 2026-07-15
