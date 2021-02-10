CREATE TABLE dbo.actions (
	id bigint NOT NULL IDENTITY(1,1),
	context varchar(255) NULL,
	[date] datetime2 NULL,
	value_date datetime2 NOT NULL,
	is_deleted bit NOT NULL,
	lynx_activity_id bigint NULL,
	[type] int NULL,
	viv_amount int NOT NULL,
	achiever_id bigint NULL,
	creator_id bigint NULL,
	payment_id bigint NULL
);
GO

ALTER TABLE dbo.actions ADD CONSTRAINT PK__actions__3213E83FE666F0EA PRIMARY KEY (id);
GO


IF OBJECT_ID('dbo.actions', 'U') IS NOT NULL AND OBJECT_ID('dbo.users', 'U') IS NOT NULL
	ALTER TABLE dbo.actions
	ADD CONSTRAINT FKocd1xgdh8ds74o2gsll7dj9kp
	FOREIGN KEY (creator_id)
	REFERENCES dbo.users (id);

IF OBJECT_ID('dbo.actions', 'U') IS NOT NULL AND OBJECT_ID('dbo.payments', 'U') IS NOT NULL
	ALTER TABLE dbo.actions
	ADD CONSTRAINT FK5t9epk5m0kxjj3acqn2tckm15
	FOREIGN KEY (payment_id)
	REFERENCES dbo.payments (id);

IF OBJECT_ID('dbo.actions', 'U') IS NOT NULL AND OBJECT_ID('dbo.users', 'U') IS NOT NULL
	ALTER TABLE dbo.actions
	ADD CONSTRAINT FKnui8mrx2atc03n9s8bg0cwa4w
	FOREIGN KEY (achiever_id)
	REFERENCES dbo.users (id);

CREATE TABLE dbo.payments (
	id bigint NOT NULL IDENTITY(1,1),
	[date] date NULL,
	viv_amount int NOT NULL,
	creator_id bigint NULL,
	receiver_id bigint NULL
);
GO

ALTER TABLE dbo.payments ADD CONSTRAINT PK__payments__3213E83FD857FB77 PRIMARY KEY (id);
GO


IF OBJECT_ID('dbo.payments', 'U') IS NOT NULL AND OBJECT_ID('dbo.users', 'U') IS NOT NULL
	ALTER TABLE dbo.payments
	ADD CONSTRAINT FK8y0kvmbql5i3hdjariwn6jh5d
	FOREIGN KEY (creator_id)
	REFERENCES dbo.users (id);

IF OBJECT_ID('dbo.payments', 'U') IS NOT NULL AND OBJECT_ID('dbo.users', 'U') IS NOT NULL
	ALTER TABLE dbo.payments
	ADD CONSTRAINT FKs3ufuyecnucmu8dbeodxq7l80
	FOREIGN KEY (receiver_id)
	REFERENCES dbo.users (id);

CREATE TABLE dbo.roles (
	id bigint NOT NULL IDENTITY(1,1),
	[type] varchar(255) NULL,
	user_id bigint NULL
);
GO

ALTER TABLE dbo.roles ADD CONSTRAINT PK__roles__3213E83F746F96AF PRIMARY KEY (id);
GO


IF OBJECT_ID('dbo.roles', 'U') IS NOT NULL AND OBJECT_ID('dbo.users', 'U') IS NOT NULL
	ALTER TABLE dbo.roles
	ADD CONSTRAINT FK97mxvrajhkq19dmvboprimeg1
	FOREIGN KEY (user_id)
	REFERENCES dbo.users (id);

CREATE TABLE dbo.user_expertises (
	id bigint NOT NULL IDENTITY(1,1),
	end_date date NULL,
	expertise varchar(255) NULL,
	start_date date NULL,
	status varchar(255) NULL,
	user_id bigint NULL
);
GO

ALTER TABLE dbo.user_expertises ADD CONSTRAINT PK__user_exp__3213E83FC934F3DC PRIMARY KEY (id);
GO


IF OBJECT_ID('dbo.user_expertises', 'U') IS NOT NULL AND OBJECT_ID('dbo.users', 'U') IS NOT NULL
	ALTER TABLE dbo.user_expertises
	ADD CONSTRAINT FK5jp02n3uj4eo1djon7e8b23os
	FOREIGN KEY (user_id)
	REFERENCES dbo.users (id);

CREATE TABLE dbo.users (
	id bigint NOT NULL IDENTITY(1,1),
	full_name varchar(255) NULL,
	viv_initial_balance int NOT NULL,
	viv_initial_balance_date datetime2 NULL,
	x4b_id varchar(255) NULL
);
GO

ALTER TABLE dbo.users ADD CONSTRAINT PK__users__3213E83FE16A46A6 PRIMARY KEY (id);
GO

