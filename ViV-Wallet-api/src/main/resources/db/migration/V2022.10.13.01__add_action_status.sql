ALTER TABLE dbo.actions ADD status varchar(255) NOT NULL DEFAULT 'TO_VALIDATE';

UPDATE dbo.actions
SET status = 'PAYABLE'
WHERE payment_id IS NOT NULL;
