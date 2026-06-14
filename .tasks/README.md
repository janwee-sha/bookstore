# Bookstore 开发任务目录

`.tasks` 是 bookstore 项目的产品需求文档目录，用于描述业务功能变更、技术改进等需求。

## 目录结构

```text
.tasks/
├── README.md
├── backlog/          # 待实施的需求
│   └── <scope>/
├── active/           # 正在推进的需求
│   └── <scope>/
└── done/             # 已完成需求的归档记录
    └── <scope>/
```

`<scope>` 优先使用模块名或影响范围命名，例如：

- `authorization`
- `book`
- `cross-service`
- `frontend`

## 文档状态变化

任务状态变化时移动文档所在目录：

- 待开始的任务存放在 `backlog/<scope>/`
- 开始实施后移动到 `active/<scope>/`
- 完成并记录验证结果后移动到 `done/<scope>/`

## 文件命名

任务文件使用三位序号加简短英文 slug：

```text
001-frontend-test-coverage.md
002-oauth-client-hardening.md
```

同一 `<scope>` 下序号只用于稳定排序，不代表全仓库唯一 ID。重命名或移动任务时，保留文档中的背景和状态记录，避免丢失上下文。

## 使用约定

- 开始实现具体任务前，先读取对应任务文档。
- 实施中发现范围变化时，更新任务文档的目标、非目标、实施步骤或状态记录。
- 这里的内容是协作和规划资料，不属于业务运行时代码、测试夹具或构建输入。