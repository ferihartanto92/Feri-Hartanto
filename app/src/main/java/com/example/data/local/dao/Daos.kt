package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.DeliveryRecordEntity
import com.example.data.local.entity.DropZoneLogEntity
import com.example.data.local.entity.RosterPersonnelEntity
import com.example.data.local.entity.VendorContractEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryRecordDao {
  @Query("SELECT * FROM delivery_records ORDER BY timestamp DESC")
  fun getAllDeliveryRecords(): Flow<List<DeliveryRecordEntity>>

  @Query("SELECT * FROM delivery_records WHERE id = :id LIMIT 1")
  fun getDeliveryRecordById(id: String): Flow<DeliveryRecordEntity?>

  @Query("SELECT * FROM delivery_records WHERE shift = :shift ORDER BY timestamp DESC LIMIT 1")
  fun getLatestByShift(shift: String): Flow<DeliveryRecordEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDeliveryRecord(record: DeliveryRecordEntity)

  @Query("DELETE FROM delivery_records WHERE id = :id")
  suspend fun deleteDeliveryRecordById(id: String)
}

@Dao
interface DropZoneLogDao {
  @Query("SELECT * FROM drop_zone_logs ORDER BY timestamp DESC")
  fun getAllDropZoneLogs(): Flow<List<DropZoneLogEntity>>

  @Query("SELECT * FROM drop_zone_logs WHERE deliveryRecordId = :deliveryId ORDER BY id ASC")
  fun getLogsByDeliveryId(deliveryId: String): Flow<List<DropZoneLogEntity>>

  @Query("SELECT * FROM drop_zone_logs WHERE shift = :shift ORDER BY timestamp DESC")
  fun getLogsByShift(shift: String): Flow<List<DropZoneLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDropZoneLogs(logs: List<DropZoneLogEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDropZoneLog(log: DropZoneLogEntity)
}

@Dao
interface RosterPersonnelDao {
  @Query("SELECT * FROM roster_personnel ORDER BY name ASC")
  fun getAllPersonnel(): Flow<List<RosterPersonnelEntity>>

  @Query("SELECT * FROM roster_personnel WHERE zone = :zone ORDER BY name ASC")
  fun getPersonnelByZone(zone: String): Flow<List<RosterPersonnelEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllPersonnel(personnel: List<RosterPersonnelEntity>)

  @Query("UPDATE roster_personnel SET isCuti = :isCuti WHERE id = :id")
  suspend fun updateCutiStatus(id: String, isCuti: Boolean)

  @Query("SELECT COUNT(*) FROM roster_personnel")
  suspend fun countPersonnel(): Int
}

@Dao
interface VendorContractDao {
  @Query("SELECT * FROM vendor_contracts")
  fun getAllVendors(): Flow<List<VendorContractEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllVendors(vendors: List<VendorContractEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateVendor(vendor: VendorContractEntity)

  @Query("SELECT COUNT(*) FROM vendor_contracts")
  suspend fun countVendors(): Int
}
