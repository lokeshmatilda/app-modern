# Transform2Container Architecture & Flow Diagrams

## System Architecture

```mermaid
graph TB
    subgraph "Client Layer"
        Client[Client Application]
    end
    
    subgraph "API Layer"
        Flask[Flask Application<br/>app.py]
        API[Transform API<br/>transform_api.py]
        ErrorHandler[Error Handlers]
    end
    
    subgraph "Service Layer"
        TransformService[Transform Service<br/>transform_service.py]
        FormService[Form Handler<br/>forms.py]
        UserMgmt[User Management<br/>user_management.py]
        ServiceDiscovery[Service Discovery<br/>service_discovery.py]
        VersionControl[Version Control<br/>version_control.py]
        Checkov[Checkov IaC Scanner<br/>checkov_iac.py]
        ServiceValidator[Service Validator<br/>service_validator.py]
    end
    
    subgraph "Business Logic Layer"
        Move2KubeCLI[Move2Kube CLI<br/>move2kube/cli.py]
        ConfigGen[Config Generator<br/>move2kube/config.py]
        FileSystem[File System Handler<br/>filesystem/_filesystem.py]
        Transformer[Custom Transformers<br/>transformer/]
    end
    
    subgraph "Data Layer"
        MongoDB[(MongoDB<br/>M2C Collection)]
        QueryManager[Query Manager<br/>db/query_manager.py]
        Models[Data Models<br/>db/models.py]
    end
    
    subgraph "External Services"
        GitRepo[Git Repository]
        UserMgmtAPI[User Management API]
        ServiceDiscoveryAPI[Service Discovery API]
    end
    
    Client --> Flask
    Flask --> API
    Flask --> ErrorHandler
    API --> TransformService
    API --> FormService
    
    TransformService --> Move2KubeCLI
    TransformService --> FileSystem
    TransformService --> QueryManager
    TransformService --> UserMgmt
    TransformService --> VersionControl
    TransformService --> ServiceValidator
    TransformService --> Checkov
    
    FormService --> QueryManager
    UserMgmt --> UserMgmtAPI
    VersionControl --> GitRepo
    ServiceDiscovery --> ServiceDiscoveryAPI
    
    QueryManager --> MongoDB
    QueryManager --> Models
    
    Move2KubeCLI --> ConfigGen
    Move2KubeCLI --> Transformer
```

## Application Transformation Flow

```mermaid
flowchart TD
    Start([Start: Create Modernize Record]) --> CreateRecord[POST /transform/modernize<br/>Create M2C Record in DB]
    CreateRecord --> SourceChoice{Source Type?}
    
    SourceChoice -->|ZIP File| ZipUpload[POST /transform/app<br/>Upload ZIP File]
    SourceChoice -->|Git Repo| GitInit[POST /transform/git-app<br/>Initialize Git Transformation]
    
    ZipUpload --> ValidateZip[Validate ZIP File<br/>Service Detection]
    ValidateZip -->|Invalid| Error1[Return Error]
    ValidateZip -->|Valid| SaveZip[Save & Extract ZIP]
    
    GitInit --> FetchCreds[Fetch Git Credentials<br/>from User Management]
    FetchCreds --> CloneRepo[Clone Git Repository]
    CloneRepo -->|Failed| Error2[Return Error]
    CloneRepo -->|Success| UpdateDB1[Update DB: Stage=SOURCE<br/>Status=COMPLETED]
    
    SaveZip --> UpdateDB1
    
    UpdateDB1 --> FormChoice{Need Custom Config?}
    
    FormChoice -->|Yes| GetForm[GET /transform/form<br/>Get Configuration Form]
    GetForm --> SubmitForm[POST /transform/form<br/>Submit Custom Configs]
    SubmitForm --> StartTransform[Start Transformation Thread]
    
    FormChoice -->|No| StartTransform
    
    StartTransform --> UpdateDB2[Update DB: Stage=TRANSFORM<br/>Status=IN_PROGRESS]
    UpdateDB2 --> GenerateConfig[Generate Move2Kube Config]
    GenerateConfig --> RunMove2Kube[Execute Move2Kube CLI<br/>Transform Command]
    
    RunMove2Kube -->|Failed| UpdateFailed[Update DB: Status=FAILED]
    RunMove2Kube -->|Success| UpdateDB3[Update DB: Stage=ARTIFACTS<br/>Status=IN_PROGRESS]
    
    UpdateDB3 --> PostProcess[Post Process Files<br/>Clean & Organize]
    PostProcess --> RunCheckov[Run Checkov IaC Scan<br/>Vulnerability Check]
    RunCheckov --> CreateArchive[Create Output ZIP Archive]
    CreateArchive --> UpdateDB4[Update DB: Stage=ARTIFACTS<br/>Status=COMPLETED<br/>app_status=SUCCESS]
    
    UpdateDB4 --> Cleanup[Cleanup Temporary Files]
    Cleanup --> End([Transformation Complete])
    
    UpdateFailed --> End
    
    End --> Download[GET /transform/download<br/>Download Transformed App]
    End --> Artifacts[GET /transform/artifacts<br/>View File Structure]
    End --> GitPush[POST /transform/push-app-to-git<br/>Push to Git Repository]
    
    style Start fill:#90EE90
    style End fill:#90EE90
    style Error1 fill:#FFB6C1
    style Error2 fill:#FFB6C1
    style UpdateFailed fill:#FFB6C1
```

## Detailed Component Flow

```mermaid
sequenceDiagram
    participant Client
    participant API as Transform API
    participant TS as Transform Service
    participant DB as MongoDB
    participant FS as File System
    participant M2K as Move2Kube CLI
    participant Checkov as Checkov Scanner
    participant Git as Git Handler
    
    Client->>API: POST /transform/modernize
    API->>TS: generateModernizeData()
    TS->>DB: Create M2C Record
    DB-->>TS: Record Created
    TS-->>API: modernize_id
    API-->>Client: Return modernize_id
    
    Client->>API: POST /transform/app (ZIP)
    API->>TS: initiate_zip_file_transformation()
    TS->>TS: Validate ZIP & Detect Service
    TS->>FS: Save & Extract ZIP
    TS->>DB: Update Record (Stage=SOURCE)
    TS-->>API: Success
    API-->>Client: Transformation Initiated
    
    Client->>API: POST /transform/form
    API->>TS: start_transformation_with_custom_configs()
    TS->>TS: Generate Configs
    TS->>DB: Update Config
    TS->>TS: start_transformation() [Thread]
    TS->>DB: Update (Stage=TRANSFORM, Status=IN_PROGRESS)
    TS->>M2K: Execute Transform Command
    M2K-->>TS: Output Directory
    TS->>DB: Update (Stage=ARTIFACTS, Status=IN_PROGRESS)
    TS->>FS: Post Process Files
    TS->>Checkov: Run IaC Scan
    Checkov-->>TS: Scan Results
    TS->>FS: Create Archive
    TS->>DB: Update (Status=COMPLETED)
    TS->>FS: Cleanup
    
    Client->>API: GET /transform/status
    API->>TS: get_transform_status_from_db()
    TS->>DB: Query Status
    DB-->>TS: Status Data
    TS-->>API: Status
    API-->>Client: Return Status
    
    Client->>API: GET /transform/download
    API->>TS: get_transformed_file()
    TS->>DB: Get M2C Object
    TS->>FS: Get Archive Path
    TS-->>API: File Path
    API-->>Client: Download ZIP
```

## Database Schema

```mermaid
erDiagram
    M2C ||--o{ StageMappings : has
    M2C ||--o{ Status : has
    
    M2C {
        string modernize_id
        string modernize_name
        string asset_id
        string organization_id
        string department_id
        string cloud_provider
        string cloud_account_id
        string service_name
        string component_id
        string host
        string port
        string stage
        string stage_status
        string app_status
        string input_file_loc
        string input_file_name
        string output_file_loc
        string output_processed_file_loc
        dict config
        dict git
        string iac_scan
        string iac_scan_archive
        datetime created_on
        datetime updated_on
        boolean is_pcf
        boolean deleted
    }
    
    StageMappings {
        string MODERNIZE
        string SOURCE
        string PLAN
        string TRANSFORM
        string ARTIFACTS
    }
    
    Status {
        string CREATED
        string STARTED
        string IN_PROGRESS
        string COMPLETED
        string FAILED
    }
```

## API Endpoints Overview

```mermaid
graph LR
    subgraph "Modernize Management"
        M1[POST /transform/modernize<br/>Create Record]
        M2[GET /transform/modernize<br/>Get Record]
        M3[POST /transform/list-view<br/>List Records]
    end
    
    subgraph "Source Input"
        S1[POST /transform/app<br/>Upload ZIP]
        S2[POST /transform/git-app<br/>Git Repo]
    end
    
    subgraph "Transformation"
        T1[GET /transform/form<br/>Get Config Form]
        T2[POST /transform/form<br/>Submit Config]
        T3[GET /transform/status<br/>Get Status]
    end
    
    subgraph "Output & Artifacts"
        O1[GET /transform/download<br/>Download ZIP]
        O2[GET /transform/artifacts<br/>File Structure]
        O3[POST /transform/file<br/>Get File Data]
        O4[POST /transform/file-edit<br/>Edit File]
    end
    
    subgraph "Git Operations"
        G1[POST /transform/push-app-to-git<br/>Push to Git]
        G2[GET /transform/publish-status<br/>Git Push Status]
    end
    
    subgraph "Management"
        D1[DELETE /transform/delete<br/>Delete & Cleanup]
        H1[GET /transform/health-check<br/>Health Check]
    end
```

## Technology Stack

- **Framework**: Flask + Flask-RESTX
- **Database**: MongoDB (via MongoEngine)
- **Transformation Engine**: Move2Kube CLI
- **IaC Scanning**: Checkov
- **Version Control**: Git (via GitHub handler)
- **Language**: Python 3.11
- **Container**: Docker (Ubuntu 22.04)

## Key Features

1. **Multi-Source Input**: Supports ZIP file uploads and Git repository cloning
2. **Service Detection**: Automatically detects service type from ZIP contents
3. **Custom Configuration**: Form-based configuration for transformation parameters
4. **Asynchronous Processing**: Thread-based transformation execution
5. **IaC Security Scanning**: Integrated Checkov for infrastructure security
6. **Git Integration**: Push transformed artifacts to Git repositories
7. **File Management**: View, edit, and download transformed artifacts
8. **Status Tracking**: Real-time status updates through database stages

