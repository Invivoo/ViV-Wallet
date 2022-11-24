UPDATE dbo.actions
SET status = 'PAYABLE'
WHERE payment_id IS NOT NULL;
