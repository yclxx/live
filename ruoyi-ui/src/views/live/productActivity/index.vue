<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="推荐活动" prop="activityId">
              <el-select v-model="queryParams.activityId" placeholder="请选择推荐活动" clearable>
                <el-option v-for="dict in activityList" :key="dict.activityId" :label="dict.activityName"
                  :value="dict.activityId">
                  <span style="float: left">{{ dict.activityName }}</span>
                  <span style="float: right;color: var(--el-text-color-secondary);font-size: 13px;">
                    {{ dict.activityId }}
                  </span>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="产品代码" prop="productCode">
              <el-input v-model="queryParams.productCode" placeholder="请输入产品代码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="queryParams.productName" placeholder="请输入产品名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="selectStatus">
              <el-select v-model="queryParams.selectStatus" placeholder="请选择状态" clearable>
                <el-option v-for="dict in live_select_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="其他条件" prop="queryType">
              <el-select v-model="queryParams.queryType" placeholder="请选择查询条件" clearable>
                <el-option v-for="dict in live_query_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="推荐日期" style="width: 308px">
              <el-date-picker v-model="dateRangeCreateTime" value-format="YYYY-MM-DD" type="daterange"
                range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Refresh" @click="handleAdd"
              v-hasPermi="['live:productActivity:add']">获取推荐</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Refresh" @click="handleUpdate()"
              v-hasPermi="['live:productActivity:edit']">刷新推荐数据</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['live:productActivity:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['live:productActivity:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="productActivityList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="推荐活动" align="center" prop="activityId" :formatter="activityFormatter" min-width="120"
          :show-overflow-tooltip="true" />
        <el-table-column label="产品代码" align="center" prop="productCode" />
        <el-table-column label="产品名称" align="center" prop="productName" />
        <el-table-column label="最新价" align="center" prop="gpInfoVo.f2" width="68" />
        <el-table-column label="涨跌幅" align="center" prop="gpInfoVo.f3" width="68" />
        <el-table-column label="最高价" align="center" prop="gpInfoVo.f15" width="68" />
        <el-table-column label="最低价" align="center" prop="gpInfoVo.f16" width="68" />
        <el-table-column label="5日均价" align="center" prop="gpInfoVo.ma5" width="78" />
        <el-table-column label="10日均价" align="center" prop="gpInfoVo.ma10" width="78" />
        <el-table-column label="20日均价" align="center" prop="gpInfoVo.ma20" width="78" />
        <el-table-column label="入选价" align="center" prop="productAmount" width="68" />
        <el-table-column label="当天价" align="center" prop="productAmountNow" width="68" />
        <el-table-column label="1天价" align="center" prop="productAmount1" width="68" />
        <el-table-column label="2天价" align="center" prop="productAmount2" width="68" />
        <el-table-column label="3天价" align="center" prop="productAmount3" width="68" />
        <el-table-column label="状态" fixed="right" align="center" prop="selectStatus" width="68">
          <template #default="scope">
            <dict-tag :options="live_select_status" :value="scope.row.selectStatus"
              @click="showChangeSelectStatus(scope.row)" />
          </template>
        </el-table-column>
        <el-table-column label="推荐日期" fixed="right" align="center" prop="productDate" width="100" />
        <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.updateTime) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <!-- 更改选择状态对话框 -->
    <el-dialog title="更改选择状态" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="productActivityFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="form.productName" :disabled="true" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="状态" prop="selectStatus">
          <el-radio-group v-model="form.selectStatus">
            <el-radio v-for="dict in live_select_status" :key="dict.value" :value="dict.value">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ProductActivity" lang="ts">
  import { listProductActivity, delProductActivity, addProductActivity, updateProductActivity, getProductActivity, updateProductActivityForm } from '@/api/live/productActivity';
  import { ProductActivityVO, ProductActivityQuery, ProductActivityForm } from '@/api/live/productActivity/types';
  import { listActivity } from '@/api/live/activity';
  import { ActivityVO } from '@/api/live/activity/types';

  const { proxy } = getCurrentInstance() as ComponentInternalInstance;
  const { live_select_status } = toRefs<any>(proxy?.useDict('live_select_status'));
  const { live_query_type } = toRefs<any>(proxy?.useDict('live_query_type'));

  const productActivityList = ref<ProductActivityVO[]>([]);
  const activityList = ref<ActivityVO[]>([]);
  const buttonLoading = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref<Array<string | number>>([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);
  const dateRangeCreateTime = ref<[DateModelType, DateModelType]>(['', '']);

  const queryFormRef = ref<ElFormInstance>();
  const productActivityFormRef = ref<ElFormInstance>();

  const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
  });

  const initFormData : ProductActivityForm = {
    id: undefined,
    activityId: undefined,
    productCode: undefined,
    productName: undefined,
    productDate: undefined,
    productAmount: undefined,
    productAmountNow: undefined,
    productAmount1: undefined,
    productAmount2: undefined,
    productAmount3: undefined,
    selectStatus: undefined,
  }
  const data = reactive<PageData<ProductActivityForm, ProductActivityQuery>>({
    form: { ...initFormData },
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      orderByColumn: 'product_date,select_status,id',
      isAsc: 'desc,asc,desc',
      activityId: undefined,
      productCode: undefined,
      productName: undefined,
      queryType: undefined,
      productDate: undefined,
      params: {
        createTime: undefined,
      }
    },
    rules: {
    }
  });

  const { queryParams, form, rules } = toRefs(data);

  const activityFormatter = (row : ActivityVO) => {
    let resultName = undefined;
    activityList.value.forEach(item => {
      if (item.activityId == row.activityId) {
        resultName = item.activityName
      }
    })
    return resultName ? resultName : row.activityId
  }

  /** 查询产品活动列表 */
  const getList = async () => {
    loading.value = true;
    queryParams.value.params = {};
    proxy?.addDateRange(queryParams.value, dateRangeCreateTime.value, 'CreateTime');
    const res = await listProductActivity(queryParams.value);
    productActivityList.value = res.rows;
    total.value = res.total;
    loading.value = false;
  }

  /** 表单重置 */
  const reset = () => {
    form.value = { ...initFormData };
    productActivityFormRef.value?.resetFields();
  }

  /** 取消按钮 */
  const cancel = () => {
    reset();
    dialog.visible = false;
  }

  /** 更改选择状态操作 */
  const showChangeSelectStatus = async (row ?: ProductActivityVO) => {
    reset();
    const _id = row?.id
    const res = await getProductActivity(_id);
    Object.assign(form.value, res.data);
    dialog.visible = true;
  }

  /** 提交按钮 */
  const submitForm = () => {
    productActivityFormRef.value?.validate(async (valid : boolean) => {
      if (valid) {
        buttonLoading.value = true;
        if (form.value.id) {
          await updateProductActivityForm(form.value).finally(() => buttonLoading.value = false);
        } else {
          proxy?.$modal.msgSuccess("操作失败");
        }
        proxy?.$modal.msgSuccess("操作成功");
        dialog.visible = false;
        await getList();
      }
    });
  }

  /** 搜索按钮操作 */
  const handleQuery = () => {
    queryParams.value.pageNum = 1;
    getList();
  }

  /** 重置按钮操作 */
  const resetQuery = () => {
    dateRangeCreateTime.value = ['', ''];
    queryFormRef.value?.resetFields();
    handleQuery();
  }

  /** 多选框选中数据 */
  const handleSelectionChange = (selection : ProductActivityVO[]) => {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  const handleAdd = async () => {
    await proxy?.$modal.confirm('是否更新数据？').finally(() => loading.value = false);
    await addProductActivity().finally(() => buttonLoading.value = false);
    proxy?.$modal.msgSuccess("操作成功");
    await getList();
  }

  /** 修改按钮操作 */
  const handleUpdate = async () => {
    await proxy?.$modal.confirm('是否更新数据？').finally(() => loading.value = false);
    await updateProductActivity().finally(() => buttonLoading.value = false);
    proxy?.$modal.msgSuccess("操作成功");
    await getList();
  }

  /** 删除按钮操作 */
  const handleDelete = async (row ?: ProductActivityVO) => {
    const _ids = row?.id || ids.value;
    await proxy?.$modal.confirm('是否确认删除产品活动编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
    await delProductActivity(_ids);
    proxy?.$modal.msgSuccess("删除成功");
    await getList();
  }

  /** 导出按钮操作 */
  const handleExport = () => {
    proxy?.download('live/productActivity/export', {
      ...queryParams.value
    }, `productActivity_${new Date().getTime()}.xlsx`)
  }

  /** 查询活动管理列表 */
  const getActivityList = async () => {
    const res = await listActivity({
      pageNum: 1,
      pageSize: 100,
      orderByColumn: 'createTime',
      isAsc: 'desc',
      status: '0',
    });
    activityList.value = res.rows;
  }

  onMounted(() => {
    getList();
    getActivityList();
  });
</script>
